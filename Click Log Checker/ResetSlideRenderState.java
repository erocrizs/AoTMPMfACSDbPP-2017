import java.awt.Graphics;
import java.util.Date;

public class ResetSlideRenderState extends SlideRenderState {

	public ResetSlideRenderState(Date timestamp) {
		super(timestamp);
	}

	@Override
	public void draw(Graphics g) {
		return;
	}

	@Override
	public double isCovered(Bug bug) {
		return 0;
	}

}
