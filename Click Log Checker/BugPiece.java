
public abstract class BugPiece {
	
	protected Vector2D topLeft, topRight, botLeft, botRight;
	protected double height, width;
	
	public BugPiece( Vector2D topLeft, Vector2D botRight ) {
		this.topLeft = topLeft;
		this.botRight = botRight;
		this.topRight = new Vector2D( botRight.getX(), topLeft.getY() );
		this.botLeft = new Vector2D( topLeft.getX(), botRight.getY() );
		
		this.width = Math.abs( topLeft.getX() - botRight.getX() );
		this.height = Math.abs( botRight.getY() - topLeft.getY() );
	}
	
	public abstract boolean isCollidingWith( MarkAction mark );
	
	public Vector2D[] getCorners() {
		return new Vector2D[] { topLeft, topRight, botRight, botLeft };
	}

	/**
	 * @return the topLeft
	 */
	public Vector2D getTopLeft() {
		return topLeft;
	}

	/**
	 * @param topLeft the topLeft to set
	 */
	public void setTopLeft(Vector2D topLeft) {
		this.topLeft = topLeft;
	}

	/**
	 * @return the topRight
	 */
	public Vector2D getTopRight() {
		return topRight;
	}

	/**
	 * @param topRight the topRight to set
	 */
	public void setTopRight(Vector2D topRight) {
		this.topRight = topRight;
	}

	/**
	 * @return the botLeft
	 */
	public Vector2D getBotLeft() {
		return botLeft;
	}

	/**
	 * @param botLeft the botLeft to set
	 */
	public void setBotLeft(Vector2D botLeft) {
		this.botLeft = botLeft;
	}

	/**
	 * @return the botRight
	 */
	public Vector2D getBotRight() {
		return botRight;
	}

	/**
	 * @param botRight the botRight to set
	 */
	public void setBotRight(Vector2D botRight) {
		this.botRight = botRight;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	
	
}
