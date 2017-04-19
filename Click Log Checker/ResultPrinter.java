import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class ResultPrinter {
	
	private File outputDirectory;
	public ResultPrinter(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void print(double[][] scores, Date[][] results, String outputFilename) throws IOException {
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

					double score = scores[i][j];
					int whole = (int) (score);
					int decimal = (int) ((score - whole) * 100 );
					String scoreText = whole + "." + decimal + (decimal<10?"0":"");
					
					bw.write( "," + hrText + ":" + minText + ":" + secText + "." + msecText + "=" + scoreText);
				} else {
					bw.write(",-----------------" );
				}
			}
			bw.write( "\n" );
		}
		
		bw.close();
	}

	public void printGroup(HashMap<String, SlideSetModel> modelMap, File outputFile) throws IOException {
		BufferedWriter bw = new BufferedWriter( new FileWriter( outputFile ) );
		BugInfo bugInfo = BugInfo.getInstance(-1);
		
		int[] bugSlides = bugInfo.getBuggedSlides();
		int[] bugPerSlide = bugInfo.getBugCountPerSlide();
		
		String header = " ";
		String subHeader = "Student ID";
		String[] outputLines = new String[ modelMap.size() ];
		
		String[] inputFileNames = modelMap.keySet().toArray( new String[ modelMap.size() ] ); 
		for(int i=0; i<modelMap.size(); i++) {
			outputLines[i] = inputFileNames[i].substring(0, inputFileNames[i].length() - 22 );
		}
		
		for( int i=0; i<bugPerSlide.length; i++ ) {
			for( int j=0; j<bugPerSlide[i]; j++ ) {
				header += "," + (i+1);
				subHeader += "," + (j+1);
				
				for( int k=0; k<outputLines.length; k++ ) {
					double[][] scores = modelMap.get( inputFileNames[k] ).getScore();
					if( bugSlides[i] >= scores.length || scores[ bugSlides[i] ][ j ] == 0) {
						outputLines[k] += ",x";
					} else {
						outputLines[k] += "," + scores[ bugSlides[i] ][j];
					}
				}
			}
		}
		
		bw.write(header + "\n");
		bw.write(subHeader + "\n");
		for(int i=0; i<modelMap.size(); i++) {
			bw.write( outputLines[i]+ "\n" );
		}
		bw.close();
	}

}
