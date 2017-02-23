import java.util.ArrayList;
import java.util.Date;

public class SlideModel {
	
	private MarkAction[] marks;
	private ResetAction[] resets;
	private Bug[] bugs;
	
	private ResetAction lastReset;
	private MarkAction[] lastMarks;
	private Date firstSeen;
	
	public SlideModel(MarkAction[] marks, ResetAction[] resets, Bug[] bugs, Date firstSeen) {
		super();
		this.marks = marks;
		this.resets = resets;
		this.bugs = bugs;
		this.firstSeen = firstSeen;
		
		this.determineFirstFound();
		if( this.resets.length > 0 )
			this.lastReset = this.resets[ this.resets.length - 1 ];
		this.lastMarks = this.isolateLastMarks();
	}
	
	private MarkAction[] isolateLastMarks() {
		
		ArrayList< MarkAction > lastMarks = new ArrayList< MarkAction >();
		for( int i = this.marks.length-1; i>=0; i-- ) {
			MarkAction current = marks[i];
			if( this.lastReset == null || current.getTimeStamp().after( lastReset.getTimeStamp() ) ) {
				lastMarks.add( current );
			} else {
				break;
			}
		}
		
		return lastMarks.toArray( new MarkAction[ lastMarks.size() ] );
	}

	public boolean[] getScore() {
		boolean[] score = new boolean[ this.bugs.length ];
		
		for( MarkAction mark: this.lastMarks ) {
			for( int i=0; i<this.bugs.length; i++ ) {
				if( score[i] )
					continue;
				
				score[i] = this.bugs[i].isCollidingWith( mark ); 
			}
		}
		
		return score;
	}
	
	public Date[] getTimedScore() {
		boolean[] score = this.getScore();
		Date[] timedScore = new Date[ score.length ];
		
		for( int i=0; i<score.length; i++ ) {
			if( !score[i] ) continue;
			
			long millisecondsTime = this.bugs[i].getFirstFound().getTime() - this.firstSeen.getTime();
			timedScore[i] = new Date( millisecondsTime );
		}
		
		return timedScore;
	}
	
	public void determineFirstFound() {
		for( int i=0; i<this.marks.length; i++ ) {
			MarkAction mark = marks[i];
			for( Bug bug: this.bugs ) {
				if( bug.getFirstFound() != null ) continue;
				
				if( bug.isCollidingWith( mark ) ) {
					bug.setFirstFound( mark.getTimeStamp() );
				}
			}
		}
	}

	/**
	 * @return the marks
	 */
	public MarkAction[] getMarks() {
		return marks;
	}

	/**
	 * @return the resets
	 */
	public ResetAction[] getResets() {
		return resets;
	}

	/**
	 * @return the bugs
	 */
	public Bug[] getBugs() {
		return bugs;
	}

	/**
	 * @return the lastReset
	 */
	public ResetAction getLastReset() {
		return lastReset;
	}

	/**
	 * @return the lastMarks
	 */
	public MarkAction[] getLastMarks() {
		return lastMarks;
	}

	/**
	 * @return the firstSeen
	 */
	public Date getFirstSeen() {
		return firstSeen;
	}

}
