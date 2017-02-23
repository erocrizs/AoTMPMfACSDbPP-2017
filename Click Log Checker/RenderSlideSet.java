import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Date;

public class RenderSlideSet {
	
	private ArrayList< RenderSlide > slides;
	private int currentSlide;
	public RenderSlideSet() {
		this.slides = new ArrayList< RenderSlide >();
		this.currentSlide = 0;
	}
	
	public void addSlide( RenderSlide renderSlide ) {
		this.slides.add( renderSlide );
	}
	
	public Date getSlideTime() {
		Date slideTime = new Date( 0l );
		if( this.slides.size() > 0 ) {
			slideTime = this.slides.get( currentSlide ).getCurrentTime();
		}
		
		return slideTime;
	}
	
	public Date getOverallTime() {
		Date overallTime = new Date( 0l );
		if( this.slides.size() > 0 ) {
			overallTime = this.slides.get( currentSlide ).getOverallTime();
		}
		
		return overallTime;
	}
	
	public int getScore() {
		int score = 0;
		if( this.slides.size() > 0 ) {
			score = this.slides.get( currentSlide ).getScore();
		}
		
		return score;
	}
	
	public void next() {
		this.currentSlide = Math.min( this.currentSlide + 1 , this.slides.size() - 1 );
	}
	
	public void prev() {
		this.currentSlide = Math.max( this.currentSlide - 1 , 0 );
	}
	
	public void step() {
		if( this.slides.size() > 0 ) {
			this.slides.get( currentSlide ).step();
		}
	}
	
	public void back() {
		if( this.slides.size() > 0 ) {
			this.slides.get( currentSlide ).back();
		}
	}
	
	public void draw( Graphics g ) {
		if( this.slides.size() > 0 ) {
			this.slides.get( currentSlide ).draw( g );
		}
	}
}
