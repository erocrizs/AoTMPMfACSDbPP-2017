import java.util.Arrays;

public class BugInfo {
	
	private static BugInfo instance;
	
	private int resolvedSlide;
	
	private int slideCount;
	private int[] buggedSlides;
	private int[] bugCountPerSlide;
	
	private BugInfo( int buggedSlideCount ) {
		this.slideCount = buggedSlideCount;
		this.buggedSlides = new int[ this.slideCount ];
		this.bugCountPerSlide = new int[ this.slideCount ];
		
		for(int i=0; i<this.slideCount; i++) {
			this.buggedSlides[i] = Integer.MAX_VALUE;
		}
		
		this.resolvedSlide = 0;
	}
	
	public void addSlide(int i) {
		this.buggedSlides[ this.resolvedSlide ] = i;
		this.bugCountPerSlide[ this.resolvedSlide ] = 0;
		this.resolvedSlide++;
	}
	
	public void addBugToSlide( int slide ) {
		int arrayIndex = Arrays.binarySearch( this.buggedSlides, slide );
		this.bugCountPerSlide[ arrayIndex ]++;
	}
	
	public void setBugCountOnSlide( int slide, int count ) {
		int arrayIndex = Arrays.binarySearch( this.buggedSlides, slide );
		this.bugCountPerSlide[ arrayIndex ] = count;
	}

	public int[] getBuggedSlides() {
		return buggedSlides;
	}

	public int[] getBugCountPerSlide() {
		return bugCountPerSlide;
	}
	
	public static BugInfo getInstance( int buggedSlideCount ) {
		if( BugInfo.instance == null ) {
			BugInfo.instance = new BugInfo( buggedSlideCount );
		}
		return BugInfo.instance;
	}
	
}
