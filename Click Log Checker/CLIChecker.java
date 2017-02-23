import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class CLIChecker {
	
	private ModelFactory factory;
	private HashMap<String, SlideSetModel> modelMap;
	private ResultPrinter printer;
	private Scanner in;
	public CLIChecker( Scanner in ) {
		this.modelMap = new HashMap<String, SlideSetModel>();
		this.in = in;
	}
	
	public void run( double horizontalAxis, double verticalAxis ) throws IOException {
		System.out.print("Language: ");
		String response = in.nextLine();
		int languageCode = -1;
		switch( response.toLowerCase() ) {
		case "java":
			languageCode = ModelFactory.LANGUAGE_JAVA;
			break;
		case "c++":
			languageCode = ModelFactory.LANGUAGE_CPP;
			break;
		case "c#":
			languageCode = ModelFactory.LANGUAGE_CSHARP;
			break;
		default:
			this.throwException(response);
		}
		factory = new ModelFactory( horizontalAxis, verticalAxis, languageCode );
		
		System.out.print("input folder: ");
		response = in.nextLine();
		File inputDirectory = new File( response );
		if( !inputDirectory.exists() || !inputDirectory.isDirectory() ) {
			this.throwException( response );
		}
		File[] inputFiles = inputDirectory.listFiles();
		for(File file: inputFiles) {
			if( file.isDirectory() ) continue;
			
			String fileName = file.getName();
			if( fileName.length() < 4) continue;
			String fileExt = fileName.substring( fileName.length() - 4 ).toLowerCase();
			if( !".csv".equals( fileExt ) ) continue;
			
			this.modelMap.put(fileName, this.factory.createModel( file ) );
		}
		
		System.out.print("output folder: ");
		response = in.nextLine();
		File outputDirectory = new File( response );
		if( !outputDirectory.exists() || !outputDirectory.isDirectory() ) {
			outputDirectory.mkdirs();
		}
		
		this.printer = new ResultPrinter( outputDirectory );
		for( String filename: this.modelMap.keySet() ) {
			SlideSetModel current = this.modelMap.get( filename );
			Date[][] results = current.getTimedScore();
			
			String outputFilename = filename.substring( 0, filename.length()-4 ) + ".out.csv";
			this.printer.print( results, outputFilename );
		}
	}

	private void throwException(String response) {
		throw new UnsupportedOperationException( response + " is invalid" );
	}
}
