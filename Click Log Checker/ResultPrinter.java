import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ResultPrinter {
	
	private File outputDirectory;
	public ResultPrinter(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void print(Date[][] results, String outputFilename) throws IOException {
		File output = new File( this.outputDirectory, outputFilename );
		if( !output.exists() ) {
			output.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter( new FileWriter( output ) );
		
		bw.write( "slide,timeBug1Found,timeBug2Found,...\n" );
		for( int i=0; i<results.length; i++ ) {
			bw.write( Integer.toString( i ) );
			for( int j=0; j<results[i].length; j++ ) {
				if( results[i][j] != null ) {
					long time = results[i][j].getTime();
					long millisecond = time % 1000;
					String msecText = ( millisecond < 10 ? "00": ( millisecond < 100 ? "0" : "" ) ) + millisecond ;
					time /= 1000;
					long second = time % 60;
					String secText = ( second < 10 ? "0":"" ) + second;
					time /= 60;
					long minute = time % 60;
					String minText = ( minute < 10 ? "0":"" ) + minute;
					time /= 60;
					long hour = time;
					String hrText = ( hour < 10 ? "0":"" ) + hour;
					
					bw.write( "," + hrText + ":" + minText + ":" + secText + "." + msecText);
				} else {
					bw.write(",------------" );
				}
			}
			bw.write( "\n" );
		}
		
		bw.close();
	}

}
