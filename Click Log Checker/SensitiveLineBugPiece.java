
public class SensitiveLineBugPiece extends BugPiece {
	private Vector2D center;
	private MarkConfig privateConfig;
	
	private double fullPoint, partialPoint;
	public SensitiveLineBugPiece(Vector2D topLeft, Vector2D botRight, double sensitivity, double partialPoint ) {
		super(topLeft, botRight);
		this.center = new Vector2D( ( topLeft.getX() + botRight.getX() ) / 2.0, ( topLeft.getY() + botRight.getY() ) / 2.0 );

		this.fullPoint = 1.0;
		this.partialPoint = partialPoint;
		
		double maxAcceptableDistance = Math.min(width, height) * sensitivity;
		this.privateConfig = new MarkConfig(maxAcceptableDistance, maxAcceptableDistance);
	}

	@Override
	public double isCollidingWith(MarkAction mark) {
		if( privateConfig.collide(mark.getPosition(), center) ) {
			return this.fullPoint;
		}
		
		if( mark.getConfig().collide(mark.getPosition(), center) ) {
			return this.partialPoint;
		}
		
		return 0;
	}

}
