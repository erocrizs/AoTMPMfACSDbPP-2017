import java.util.ArrayList;
import java.util.Date;

public class SlideSetModel {
	
	private ArrayList<SlideModel> slides;

	public SlideSetModel() {
		this.slides = new ArrayList<SlideModel>();
	}

	public void addSlide( SlideModel slide ) {
		this.slides.add( slide );
	}
	
	public boolean[][] getScore() {
		boolean[][] scores = new boolean[ this.slides.size() ][0];
		for( int i=0; i<this.slides.size(); i++ ) {
			scores[i] = this.slides.get( i ).getScore();
		}
		return scores;
	}
	
	public Date[][] getTimedScore() {
		Date[][] timedScore = new Date[ this.slides.size() ][0];
		for( int i=0; i<this.slides.size(); i++ ) {
			timedScore[i] = this.slides.get( i ).getTimedScore();
		}
		return timedScore;
	}
	
	public SlideModel[] getSlides() {
		return this.slides.toArray( new SlideModel[ this.slides.size() ] );
	}
}
