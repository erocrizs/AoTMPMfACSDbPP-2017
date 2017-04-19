import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;

public class ModelFactory {

	public static final int LANGUAGE_JAVA = (1<<2);
	public static final int LANGUAGE_CPP = (1<<4);
	public static final int LANGUAGE_CSHARP = (1<<8);
	
	private MarkConfig markConfig;
	private FileInputParser parser;
	private Adjuster adjuster;
	private ArrayList<Bug[]> bugs;
	
	private double lineBugSensitivity, lineBugPartialPt;
	private double codeBugSensitivity, codeBugPartialPt;
	public ModelFactory( double horizontalAxis, double verticalAxis ) {
		this.markConfig = new MarkConfig( horizontalAxis, verticalAxis );
		this.parser = new FileInputParser();
	}
	
	public void setupLanguage(int language) {
		switch( language ) {
		case ModelFactory.LANGUAGE_CPP:
			this.setupCPP();
			break;
		case ModelFactory.LANGUAGE_CSHARP:
			this.setupCSharp();
			break;
		case ModelFactory.LANGUAGE_JAVA:
			this.setupJava();
			break;
		}
	}
	
	private void setupJava() {
		this.adjuster = new AdduAdjuster( null );
		this.readBugList( "ans-key" + File.separator + "Java.anskey" );
	}

	private void setupCSharp() {
		this.adjuster = new AdduAdjuster( null );
		this.readBugList( "ans-key" + File.separator + "C#.anskey" );
	}

	private void setupCPP() {
		this.adjuster = new AdduAdjuster( null );
		this.readBugList( "ans-key" + File.separator + "C++.anskey" );
	}
	
	public void setLineBugConfig( double sensitivity, double partialPoint ) {
		this.lineBugSensitivity = sensitivity;
		this.lineBugPartialPt = partialPoint;
	}
	
	public void setCodeBugConfig( double sensitivity, double partialPoint ) {
		this.codeBugSensitivity = sensitivity;
		this.codeBugPartialPt = partialPoint;
	}
	
	public void readBugList( String path ) {
		File toRead = new File( path );
		String[][] tokens = null;
		try {
			tokens = this.parser.parseConfig( toRead );
		} catch( IOException e ) {
			e.printStackTrace();
			throw new UnsupportedOperationException("Error in reading the config file");
		}
		
		ArrayDeque<String> tokenQueue = new ArrayDeque<String>();
		for(int i=0; i<tokens.length; i++) {
			for(int j=0; j<tokens[i].length; j++) {
				if( tokens[i][j].length() > 0 )
					tokenQueue.add( tokens[i][j] );
			}
		}
		
		ArrayList<Bug[]> tempBugs = new ArrayList<Bug[]>();
		int buggedSlide = Integer.parseInt( tokenQueue.poll() );
		BugInfo bugInfo = BugInfo.getInstance( buggedSlide );
		while( buggedSlide-->0 ) {
			int slideNo = Integer.parseInt( tokenQueue.poll() );
			int bugCount = Integer.parseInt( tokenQueue.poll() );
			bugInfo.addSlide( slideNo );
			bugInfo.setBugCountOnSlide( slideNo, bugCount );;
			
			while( tempBugs.size() <= slideNo ) {
				tempBugs.add( null );
			}
			Bug[] slideBugs = new Bug[ bugCount ];
			for(int i=0; i<bugCount; i++) {
				int bugPieceCount = Integer.parseInt( tokenQueue.poll() );
				BugPiece[] bugPieces = new BugPiece[ bugPieceCount ];
				for(int j=0; j<bugPieceCount; j++) {
					String type = tokenQueue.poll();
					
					int x1 = Integer.parseInt( tokenQueue.poll() );
					int y1 = Integer.parseInt( tokenQueue.poll() );
					Vector2D topLeft = new Vector2D(x1, y1);
					int x2 = Integer.parseInt( tokenQueue.poll() );
					int y2 = Integer.parseInt( tokenQueue.poll() );
					Vector2D botRight = new Vector2D(x2, y2);
					switch(type) {
					case "L":
						bugPieces[j] = new SensitiveLineBugPiece(topLeft, botRight, lineBugSensitivity, lineBugPartialPt);
						break;
					case "C":
						bugPieces[j] = new SensitiveCodeBugPiece(topLeft, botRight, codeBugSensitivity, codeBugPartialPt);
						break;
					default:
						throw new UnsupportedOperationException( toRead.getName() + ": " + type +  " is an invalid bug type" );
					}
				}
				slideBugs[i] = new Bug( bugPieces );
			}
			tempBugs.set(slideNo, slideBugs);
		}
		for(int i=0; i<tempBugs.size(); i++) {
			if( tempBugs.get(i) == null ) {
				tempBugs.set(i, new Bug[0]);
			}
		}
		this.bugs = tempBugs;
	}
	
	public SlideSetModel createModel( File file ) throws IOException {
		String[][] tokens = this.parser.parseInput( file );
		if( this.adjuster != null )
			this.adjuster.adjust( tokens );
		
		ArrayList< ArrayList<MarkAction> > marks = new ArrayList< ArrayList<MarkAction> >();
		ArrayList< ArrayList<ResetAction> > resets = new ArrayList< ArrayList<ResetAction> >();
		ArrayList< Date > firstSeens = new ArrayList< Date >();
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
		for(int i=0; i<tokens.length; i++) {
			String[] line= tokens[i];
			Date timestamp = new Date();
			int slide = Integer.parseInt( line[2] );
			try {
				timestamp = formatter.parse( line[1] );
			} catch( Exception e ) {} 
			
			if( i == 0 ) {
				firstSeens.add( timestamp );
				marks.add( new ArrayList<MarkAction>() );
				resets.add( new ArrayList<ResetAction>() );
			}
			
			if( "Next".equals( line[0] ) || "Previous".equals( line[0] ) ) {
				int destination = slide;
				if( i < tokens.length-1 ) {
					destination = Integer.parseInt( tokens[i+1][2] );
				}
				
				while( marks.size() <= destination ) {
					marks.add( new ArrayList<MarkAction>() );
					resets.add( new ArrayList<ResetAction>() );
					firstSeens.add( null );
				}
				while( bugs.size() <= destination ) {
					bugs.add( new Bug[0] );
				}
				if( firstSeens.get(destination) == null )
					firstSeens.set(destination, timestamp);
			} else if( "Reset".equals( line[0] ) ) {
				resets.get( slide ).add( new ResetAction( timestamp ) );
			} else if( "Mark".equals( line[0] ) ) {
				int xPos = Integer.parseInt( line[3] );
				int yPos = Integer.parseInt( line[4] );
				Vector2D position = new Vector2D( xPos, yPos );
				marks.get( slide ).add( new MarkAction( timestamp, position, this.markConfig ) );
			} else {
				throw new UnsupportedOperationException( line[0] + ": invalid operation" );
			}
		}
		
		SlideSetModel model = new SlideSetModel();
		int slideCount = marks.size();
		for(int i=0; i<slideCount; i++) {
			ArrayList<MarkAction> slideMarkAL = marks.get(i);
			ArrayList<ResetAction> slideResetAL = resets.get(i);
			Bug[] slideBugOriginal = this.bugs.get(i);
			Bug[] slideBug = new Bug[ slideBugOriginal.length ];
			for(int j=0; j<slideBugOriginal.length; j++) {
				slideBug[j] = slideBugOriginal[j].clone();
			}
			MarkAction[] slideMark = slideMarkAL.toArray( new MarkAction[slideMarkAL.size()] );
			ResetAction[] slideReset = slideResetAL.toArray( new ResetAction[slideResetAL.size()] );
			SlideModel slide = new SlideModel(slideMark, slideReset, slideBug, firstSeens.get(i) );
			model.addSlide( slide );
		}
		
		return model;
	}
}
