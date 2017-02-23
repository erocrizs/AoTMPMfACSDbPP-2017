
public class EncloseTypeBugPiece extends BugPiece {

	public EncloseTypeBugPiece(Vector2D topLeft, Vector2D botRight) {
		super(topLeft, botRight);
	}

	@Override
	public boolean isCollidingWith(MarkAction mark) {
		MarkConfig checker = mark.getConfig();
		Vector2D[] toCheck = this.getCorners();
		
		boolean colliding = true;
		for( Vector2D corner: toCheck ) {
			if( !checker.collide( mark.getPosition(), corner ) ) {
				colliding = false;
				break;
			}
		}
		
		return colliding;
	}

}
