
public class AdduAdjuster extends Adjuster {

	double offsetX, rateX, offsetY, rateY;
	public AdduAdjuster(Adjuster next) {
		super(next);
		this.offsetX = -0.8695;
		this.rateX = 1.044;
		this.offsetY = -0.1366;
		this.rateY = 0.9444;
	}

	@Override
	public void applyAdjustment(String[][] input) {
		for(int i=0; i<input.length; i++) {
			if( !"Mark".equals( input[i][0] ) )
				continue;
			
			double x = Integer.parseInt( input[i][3] );
			double y = Integer.parseInt( input[i][4] );
			
			double newX = offsetX + (rateX * x);
			double newY = offsetY + (rateY * y);
			
			input[i][3] = Integer.toString( (int)( Math.round( newX ) ) );
			input[i][4] = Integer.toString( (int)( Math.round( newY ) ) );
		}
	}

}
