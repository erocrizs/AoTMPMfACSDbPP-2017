
public class TouchTypeBugPiece extends BugPiece {

	private final double EPS = 1e-7;
	
	public TouchTypeBugPiece(Vector2D topLeft, Vector2D botRight) {
		super(topLeft, botRight);
	}

	@Override
	public double isCollidingWith(MarkAction mark) {
		double nearestX = Math.max( this.topLeft.getX(), Math.min( this.topRight.getX(), mark.getPosition().getX() ) );
		double nearestY = Math.max( this.topLeft.getY(), Math.min( this.botRight.getY(), mark.getPosition().getY() ) );
		
		if ( this.nearEnough( nearestX, mark.getPosition().getX() ) &&
			this.nearEnough( nearestY, mark.getPosition().getY() ) ) {
			return 1;
		}
		
		return 0;
	}
	
	private boolean nearEnough( double a, double b ) {
		return Math.abs( a - b ) < EPS;
	}
	
}
