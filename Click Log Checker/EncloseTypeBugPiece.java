
public class EncloseTypeBugPiece extends BugPiece {

	public EncloseTypeBugPiece(Vector2D topLeft, Vector2D botRight) {
		super(topLeft, botRight);
	}

	@Override
	public double isCollidingWith(MarkAction mark) {
		MarkConfig checker = mark.getConfig();
		Vector2D[] toCheck = this.getCorners();
		
		double colliding = 1;
		for( Vector2D corner: toCheck ) {
			if( !checker.collide( mark.getPosition(), corner ) ) {
				colliding = 0;
				break;
			}
		}
		
		return colliding;
	}

}
