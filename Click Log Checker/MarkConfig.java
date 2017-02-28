

public class MarkConfig {
	
	private double a;
	private double b;
	
	public MarkConfig( double horizontalAxis, double verticalAxis ) {
		this.a = horizontalAxis / 2.0;
		this.b = verticalAxis / 2.0;
		
	}
	
	public boolean collide( Vector2D ellipseCenter, Vector2D point ) {
		Vector2D difference = new Vector2D( point.getX()-ellipseCenter.getX(), point.getY()-ellipseCenter.getY() );
		
		double firstTermNumerator = difference.getX() * difference.getX();
		double firstTermDenominator = a * a;
		
		double secondTermNumerator = difference.getY() * difference.getY();
		double secondTermDenominator = b * b;
		
		double firstTerm = firstTermNumerator / firstTermDenominator;
		double secondTerm = secondTermNumerator / secondTermDenominator;
		double eval = firstTerm + secondTerm;
		
		return eval <= 1;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}
	
	
}