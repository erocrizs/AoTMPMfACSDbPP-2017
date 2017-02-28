import java.awt.Graphics;
import java.util.Date;

public abstract class SlideRenderState {
	
	protected Date timestamp;
	public SlideRenderState( Date timestamp ) {
		 this.timestamp = timestamp;
	}
	
	public abstract void draw( Graphics g );

	public Date getTimestamp() {
		return timestamp;
	}
	
	public abstract double isCovered( Bug bug );
}
