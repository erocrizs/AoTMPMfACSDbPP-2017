import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.filechooser.FileFilter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUIVisualizer extends JFrame {

	private int width, height;
	private int canvasMinWidth, canvasMinHeight;
	private JPanel canvas;
	
	private JButton stepButton, backButton, nextButton, prevButton;
	private JLabel slideTimeLabel, overallTimeLabel, scoreLabel;
	
	private Object[] languageOptions;
	private double ellipseWidth, ellipseHeight;
	
	private ModelFactory modelFactory;
	private SlideSetModel slideSetModel;
	
	private RenderFactory renderFactory;
	private RenderSlideSet renderSlideSet;
	
	private Vector2D mousePosition;
	private Vector2D dragBoxCornerA;
	private Vector2D dragBoxCornerB;
	
	public GUIVisualizer( double ellipseWidth, double ellipseHeight ) {
		this.width = 1366;
		this.height = 769;
		this.ellipseWidth = ellipseWidth;
		this.ellipseHeight = ellipseHeight;
		
		this.languageOptions = new Object[] { "C#", "C++", "Java" };
		
		this.canvasMinWidth = 1200;
		this.canvasMinHeight = 600;
		
		this.setGUI();
		this.setListeners();
		
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setResizable(false);
		this.requestFocus();
		this.setVisible(true);
	}

	private void setGUI() {
		this.setSize( this.width, this.height );
		this.setLayout( new BorderLayout() );
		
		this.canvas = new JPanel() {
			@Override
			public void paintComponent( Graphics g ) {
				GUIVisualizer.this.paintOverrider( g );
			}
		};

		this.add( this.canvas, "Center" );

		this.canvas.setMinimumSize( new Dimension( this.canvasMinWidth, this.canvasMinHeight ) );

		JPanel control = new JPanel( new GridLayout( 1, 5, 10, 10 ) );

		JPanel progressControl = new JPanel( new GridLayout( 1, 2, 2, 2 ) );
		this.stepButton = new JButton( "Step" );
		this.backButton = new JButton( "Back" );
		progressControl.add( this.backButton );
		progressControl.add( this.stepButton );

		this.slideTimeLabel = new JLabel( "Slide Time: 00:00:00.000" );
		this.overallTimeLabel = new JLabel( "Overall Time: 00:00:00.000");
		this.scoreLabel = new JLabel( "Score: 0" );

		JPanel slideControl = new JPanel( new GridLayout( 1, 2, 2, 2 ) );
		this.prevButton = new JButton( "Prev" );
		this.nextButton = new JButton( "Next" );
		slideControl.add( this.prevButton );
		slideControl.add( this.nextButton );

		control.add( progressControl );
		control.add( this.slideTimeLabel );
		control.add( this.overallTimeLabel );
		control.add( this.scoreLabel );
		control.add( slideControl );

		this.add( control, "North" );
	}

	private void setListeners() {
		this.stepButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				GUIVisualizer.this.renderSlideSet.step();
				GUIVisualizer.this.refreshInfo();
			}
		} );

		this.backButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				GUIVisualizer.this.renderSlideSet.back();
				GUIVisualizer.this.refreshInfo();
			}
		} );

		this.nextButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				GUIVisualizer.this.renderSlideSet.next();
				GUIVisualizer.this.refreshInfo();
			}
		} );

		this.prevButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				GUIVisualizer.this.renderSlideSet.prev();
				GUIVisualizer.this.refreshInfo();
			}
		} );
		
		this.canvas.addMouseMotionListener( new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				GUIVisualizer.this.dragBoxCornerB = new Vector2D( e.getX(), e.getY() );
				GUIVisualizer.this.canvas.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				GUIVisualizer.this.mousePosition = new Vector2D( e.getX(), e.getY() );
				GUIVisualizer.this.canvas.repaint();
			}
			
		} );
		
		this.canvas.addMouseListener( new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				GUIVisualizer.this.dragBoxCornerA = new Vector2D( e.getX(), e.getY() );
				GUIVisualizer.this.dragBoxCornerB = GUIVisualizer.this.dragBoxCornerA;
				GUIVisualizer.this.canvas.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				GUIVisualizer.this.dragBoxCornerB = new Vector2D( e.getX(), e.getY() );
				
				int x0, y0, x1, y1;
				x0 = (int) GUIVisualizer.this.dragBoxCornerA.getX();
				y0 = (int) GUIVisualizer.this.dragBoxCornerA.getY();
				x1 = (int) GUIVisualizer.this.dragBoxCornerB.getX();
				y1 = (int) GUIVisualizer.this.dragBoxCornerB.getY();
				GUIVisualizer.this.canvas.repaint();
				
				System.out.printf( "Rectangle Corners: %d %d %d %d\n",
						Math.min(x0, x1),
						Math.min(y0, y1),
						Math.max(x0, x1),
						Math.max(y0,  y1) );
			}
		
		} );
	}

	private void paintOverrider(Graphics g) {
		g.setColor( Color.BLACK );
		g.fillRect( 0, 0, this.width, this.height);
		
		if( this.renderSlideSet != null ) {
			this.renderSlideSet.draw( g );;
		}

		if( this.mousePosition != null ) {
			int xOval = (int) ( this.mousePosition.getX() - (this.ellipseWidth / 2.0) );
			int yOval = (int) ( this.mousePosition.getY() - (this.ellipseHeight / 2.0) );
			
			g.setColor( Color.YELLOW );
			g.drawOval( xOval, yOval, (int) this.ellipseWidth, (int) this.ellipseHeight);
		}
		
		if( this.dragBoxCornerA != null && this.dragBoxCornerB != null ) {
			int xBox = (int) Math.min( this.dragBoxCornerA.getX(), this.dragBoxCornerB.getX() );
			int yBox = (int) Math.min( this.dragBoxCornerA.getY(), this.dragBoxCornerB.getY() );
			int boxHeight = (int) Math.abs( this.dragBoxCornerA.getY() - this.dragBoxCornerB.getY() );
			int boxWidth = (int) Math.abs( this.dragBoxCornerA.getX() - this.dragBoxCornerB.getX() );
			
			g.setColor( Color.GREEN );
			g.drawRect( xBox, yBox, boxWidth, boxHeight);
		}
	}

	private void refreshInfo() {
		String slideTimeText = this.transformDate( this.renderSlideSet.getSlideTime() );
		this.slideTimeLabel.setText( "Slide Time: " + slideTimeText );
		
		String overallTimeText = this.transformDate( this.renderSlideSet.getOverallTime() );
		this.overallTimeLabel.setText( "Overall Time: " + overallTimeText );
		
		String scoreText = Integer.toString( this.renderSlideSet.getScore() );
		this.scoreLabel.setText( "Score: " + scoreText );
		
		this.canvas.repaint();
	}
	
	private String transformDate( Date date ) {
		long time = date.getTime();
		
		long milliseconds = time % 1000;
		String msText = Long.toString( milliseconds );
		if( milliseconds < 10 ) {
			msText = "00" + msText;
		} else if( milliseconds < 100 ) {
			msText = "0" + msText;
		}
		time /= 1000;
		
		long seconds = time % 60;
		String secText = Long.toString( seconds );
		if( seconds < 10 ) {
			secText = "0" + secText;
		}
		time /= 60;
		
		long minutes = time % 60;
		String minText = Long.toString( minutes );
		if( minutes < 10 ) {
			minText = "0" + minText;
		}
		time /= 60;
		
		long hour = time;
		String hrText = Long.toString( hour );
		if( hour < 10 ) {
			hrText = "0" + hrText;
		}
		
		return ( hrText + ":" + minText + ":" + secText + "." + msText );
	}
	
	public void run() throws IOException {
		int languageCode = -1;
		String language = (String) JOptionPane.showInputDialog(
	                this,
	                "Choose Language:",
	                "Customized Dialog",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                this.languageOptions,
	                "Java");
	
		switch( language ) {
		case "C++":
			languageCode = ModelFactory.LANGUAGE_CPP;
			break;
		case "C#":
			languageCode = ModelFactory.LANGUAGE_CSHARP;
			break;
		case "Java":
			languageCode = ModelFactory.LANGUAGE_JAVA;
			break;
		default:
			throw new UnsupportedOperationException( language + " is not valid!" );
		}
		
		this.modelFactory = new ModelFactory( this.ellipseWidth, this.ellipseHeight, languageCode );
		
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setDialogTitle( "Choose which log to process" );
		fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
		fileChooser.setMultiSelectionEnabled( false );		
		fileChooser.setFileFilter( new FileFilter() {
			@Override
			public String getDescription() {
				return "Comma-Separated Value (CSV) files";
			}

			@Override
			public boolean accept(File file) {
				if( file.isDirectory() )
					return true;

				String fileName = file.getName();
				if( fileName.length() < 4)
					return false;

				String fileExt = fileName.substring( fileName.length() - 4 ).toLowerCase();
				if( !".csv".equals( fileExt ) )
					return false;

				return true;
			}

		} );

		int status = fileChooser.showOpenDialog( this );
		if( status != JFileChooser.APPROVE_OPTION ) {
			System.exit(0);
		}
		
		File inputFile = fileChooser.getSelectedFile();
		this.slideSetModel = this.modelFactory.createModel( inputFile );
		this.renderFactory = new RenderFactory( this.slideSetModel, languageCode, new Vector2D( 1366, 679 ) );
		this.renderSlideSet = this.renderFactory.createSlideSet();
		
		this.refreshInfo();
	}

}
