import java.util.Date;

public class Bug {

	private BugPiece[] bugPieces;
	private Date firstFound;
	
	public Bug( BugPiece[] bugPieces ) {
		this.bugPieces = bugPieces;
	}
	
	public boolean isCollidingWith( MarkAction mark ) {
		boolean colliding = false;
		
		for( BugPiece bug: this.bugPieces ) {
			if( bug.isCollidingWith( mark ) ) {
				colliding = true;
				break;
			}
		}
		
		return colliding;
	}

	/**
	 * @return the firstFound
	 */
	public Date getFirstFound() {
		return firstFound;
	}

	/**
	 * @param firstFound the firstFound to set
	 */
	public void setFirstFound(Date firstFound) {
		this.firstFound = firstFound;
	}

	@Override
	public Bug clone() {
		return new Bug( this.bugPieces );
	}

	public BugPiece[] getBugPieces() {
		return bugPieces;
	}
	
}
