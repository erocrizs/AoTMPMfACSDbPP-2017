import java.awt.Color;
import java.awt.Graphics;

public class MarkedSlideRenderState extends SlideRenderState {

	private MarkAction markAction;
	private SlideRenderState next;
	public MarkedSlideRenderState( MarkAction mark, SlideRenderState next ) {
		super( mark.getTimeStamp() );
		this.markAction = mark;
		this.next = next;
	}

	@Override
	public void draw(Graphics g) {
		Vector2D center = markAction.getPosition();
		double horizontalAxis = markAction.getConfig().getA();
		double verticalAxis = markAction.getConfig().getB();
		int x = (int) ( center.getX() - horizontalAxis );
		int y = (int) ( center.getY() - verticalAxis );
		
		g.setColor( Color.RED );
		g.fillOval((int) center.getX() - 1, (int) center.getY() - 1, 2, 2 );
		g.drawOval(x, y, (int) ( horizontalAxis * 2 ), (int) ( verticalAxis * 2 ) );
		
		if( this.next != null )
			next.draw(g);
	}

	@Override
	public double isCovered(Bug bug) {
		if( bug.isCollidingWith( this.markAction ) > 0 )
			return bug.isCollidingWith( this.markAction );
		
		if( this.next != null )
			return this.next.isCovered( bug );
		
		return 0;
	}

}
