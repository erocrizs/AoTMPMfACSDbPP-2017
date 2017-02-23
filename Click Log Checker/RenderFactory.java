import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.Date;

import javax.imageio.ImageIO;

public class RenderFactory {

	public static final int LANGUAGE_JAVA = (1<<2);
	public static final int LANGUAGE_CPP = (1<<4);
	public static final int LANGUAGE_CSHARP = (1<<8);
	
	private SlideSetModel model;
	private int language;
	private Vector2D dimensions;
	
	public RenderFactory( SlideSetModel model, int language, Vector2D dimensions ) {
		this.model = model;
		this.language = language;
		this.dimensions = dimensions;
	}
	
	public RenderSlideSet createSlideSet() throws IOException {
		RenderSlideSet slideSet = new RenderSlideSet();
		
		SlideModel[] slideModels = this.model.getSlides();
		Date baseTime = slideModels[0].getFirstSeen();
		BufferedImage[] images = this.fetchImages( slideModels.length );
		
		for( int i=0; i<slideModels.length; i++ ) {
			slideSet.addSlide( new RenderSlide( slideModels[i], images[i], this.dimensions, baseTime ) );
		}
		
		return slideSet;
	}
	
	private BufferedImage[] fetchImages( int size ) {
		BufferedImage[] images = new BufferedImage[ size ];
		
		String languageName = "";
		switch( this.language ) {
		case LANGUAGE_CPP:
			languageName = "C++";
			break;
		case LANGUAGE_CSHARP:
			languageName = "C#";
			break;
		case LANGUAGE_JAVA:
			languageName = "Java";
			break;
		}
		
		File imageDirectory = new File( "slide-images" + File.separator + languageName );
		for( int i=0; i<size; i++ ) {
			File image = new File( imageDirectory, i + ".jpg" );
			try {
				images[i] = ImageIO.read( image );
			} catch (IOException e) {
				images[i] = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
			}
		}
		
		return images;
	}

}
