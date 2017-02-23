import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileInputParser {
	public String[][] parseInput( File input ) throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( input ) );
		
		String line = br.readLine();
		String[] tokens = line.split(" ");
		
		line = br.readLine();
		if( !"Username:".equals( tokens[0] ) || !"action_type,timestamp,slide,x,y". equals( line ) ) {
			br.close();
			throw new UnsupportedOperationException( input.getName() + " is invalid" );
		}
		
		line = br.readLine();
		ArrayList< String[] > parsed = new ArrayList< String[] >();
		while( line != null ) {
			tokens = line.split(",");
			parsed.add( tokens );
			line = br.readLine();
		}
		br.close();
		
		String[][] output = new String[parsed.size()][0];
		for(int i=0; i<parsed.size(); i++) {
			output[i] = parsed.get(i);
		}
		return output;
	}
	
	public String[][] parseConfig( File input ) throws IOException {
		BufferedReader br = new BufferedReader( new FileReader( input ) );
		
		String line = br.readLine();
		
		if( !"--ANS-KEY--".equals( line ) ) {
			br.close();
			throw new UnsupportedOperationException( input.getName() + " is invalid" );
		}
		
		line = br.readLine();
		ArrayList< String[] > parsed = new ArrayList< String[] >();
		while( line != null ) {
			String[] tokens = line.split(" ");
			parsed.add( tokens );
			line = br.readLine();
		}
		br.close();
		
		String[][] output = new String[parsed.size()][0];
		for(int i=0; i<parsed.size(); i++) {
			output[i] = parsed.get(i);
		}
		return output;
	}
}
