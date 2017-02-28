import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;


public class RenderSlide {

	private Bug[] bugs;
	private ArrayList< SlideRenderState > states;
	private SlideRenderState currentState;
	private BufferedImage image;
	private Vector2D dimensions;
	private int stateIndex;
	private double score;
	
	private Date firstSeen;
	private Date currentTime;
	private Date baseTime;
	private Date overallTime;
	public RenderSlide( SlideModel slide, BufferedImage image, Vector2D dimensions, Date baseTime ) {
		this.bugs = slide.getBugs();
		this.score = 0;
		this.firstSeen = slide.getFirstSeen();
		this.currentTime = new Date( 0l );
		this.baseTime = baseTime;
		this.overallTime = new Date( this.firstSeen.getTime() - this.baseTime.getTime() );
		this.image = image;
		this.dimensions = dimensions;
		this.stateIndex = 0;
		
		this.states = new ArrayList< SlideRenderState >();
		states.add( new ResetSlideRenderState( this.firstSeen ) );
		
		ArrayDeque< MarkAction > marks = new ArrayDeque< MarkAction > ();
		for(MarkAction mark: slide.getMarks()) {
			marks.add( mark );
		}
		ArrayDeque< ResetAction > resets = new ArrayDeque< ResetAction > ();
		for(ResetAction reset: slide.getResets()) {
			resets.add( reset );
		}
		
		while( !marks.isEmpty() || !resets.isEmpty() ) {
			if( !marks.isEmpty() && resets.isEmpty() ) {
				this.addState( marks.poll() );
			} else if( marks.isEmpty() && !resets.isEmpty() ) {
				this.addState( resets.poll() );
			} else {
				MarkAction mark = marks.peek();
				ResetAction reset = resets.peek();
				if( mark.compareTo( reset ) < 0 ) {
					this.addState( marks.poll() );
				} else {
					this.addState( resets.poll() );
				}
			}
		}
		this.currentState = this.states.get(0);
	}
	
	private void addState( ResetAction action ) {
		this.states.add( new ResetSlideRenderState( action.getTimeStamp() ) );
	}
	
	private void addState( MarkAction action ) {
		SlideRenderState lastState = null;
		if( !this.states.isEmpty() ) {
			lastState = this.states.get( this.states.size() - 1 );
		}
		states.add( new MarkedSlideRenderState( action, lastState ) );
	}
	
	public void draw( Graphics g ) {
		g.drawImage( this.image, 0, 0, (int) this.dimensions.getX(), (int) this.dimensions.getY(), null);
		g.setColor( Color.BLUE );
		for( Bug bug: this.bugs ) {
			for( BugPiece bugPiece: bug.getBugPieces() ) {
				int x = (int) bugPiece.getTopLeft().getX();
				int y = (int) bugPiece.getTopLeft().getY();
				int w = (int) bugPiece.getBotRight().getX() - x;
				int h = (int) bugPiece.getBotRight().getY() - y;
				g.drawRect(x, y, w, h);
			}
		}
		this.currentState.draw( g );
	}
	
	public void step() {
		if( this.stateIndex + 1 >= this.states.size() )
			return;
		
		this.stateIndex += 1;
		this.updateStateInfo();
		
	}
	
	public void back() {
		if( this.stateIndex - 1 < 0 )
			return;
		
		this.stateIndex -= 1;
		this.updateStateInfo();
	}
	
	private void updateStateInfo() {
		this.currentState = this.states.get( this.stateIndex );
		this.recomputeScore();
		this.currentTime = new Date( this.currentState.getTimestamp().getTime() - this.firstSeen.getTime() );
		this.overallTime = new Date( this.currentState.getTimestamp().getTime() - this.baseTime.getTime() );
	}

	private void recomputeScore() {
		double tempScore = 0;
		for( Bug bug: this.bugs ) {
			tempScore += this.currentState.isCovered(bug);
		}
		this.score = tempScore;
	}

	public int getStateIndex() {
		return stateIndex;
	}

	public double getScore() {
		return score;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public Date getOverallTime() {
		return overallTime;
	}
	
	
}
