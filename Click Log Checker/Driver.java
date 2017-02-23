import java.io.IOException;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner( System.in );
		
		System.out.println( "[1] - CLI Mass Checker" );
		System.out.println( "[2] - GUI Input Visualizer" );
		System.out.print( "Select operation type: " );
		
		String response = in.nextLine();
		if( "1".equals( response ) ) {
			CLIChecker cc = new CLIChecker( in );
			cc.run( 150, 72 );
		} else if( "2".equals( response ) ) {
			GUIVisualizer gv = new GUIVisualizer( 150, 72 );
			gv.run();
		} else {
			in.close();
			throw new UnsupportedOperationException( "Choose only between 1 and 2" );
		}
		in.close();
	}

}
