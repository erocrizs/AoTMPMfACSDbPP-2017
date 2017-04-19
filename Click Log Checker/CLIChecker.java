import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

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
		factory = new ModelFactory( horizontalAxis, verticalAxis );
		
		double sensitivity, partialPt;
		sensitivity = 1.00;
		partialPt = 0.5;
		System.out.println();
		System.out.println("# Line Bug Hitbox Config");
		System.out.print("* Hitbox Tolerance (1 to 100) (press ENTER for 100): ");
		response = in.nextLine();
		if(response.trim().length() != 0) {
			sensitivity = Double.parseDouble( response ) / 100.0;
		}
		System.out.print("* Partial Points (0.0 to 1.0) (press ENTER for 0.5): ");
		response = in.nextLine();
		if(response.trim().length() != 0) {
			partialPt = Double.parseDouble( response );
		}
		factory.setLineBugConfig(sensitivity, partialPt);

		sensitivity = 1.00;
		partialPt = 0.5;
		System.out.println();
		System.out.println("# Code Bug Hitbox Config");
		System.out.print("* Hitbox Tolerance (1 to 100) (press ENTER for 100): ");
		response = in.nextLine();
		if(response.trim().length() != 0) {
			sensitivity = Double.parseDouble( response ) / 100.0;
		}
		System.out.print("* Partial Points (0.0 to 1.0) (press ENTER for 0.5): ");
		response = in.nextLine();
		if(response.trim().length() != 0) {
			partialPt = Double.parseDouble( response );
		}
		factory.setCodeBugConfig(sensitivity, partialPt);
		factory.setupLanguage( languageCode );
		
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
		
		System.out.print("output type (indiv/group) (press ENTER for indiv): ");
		response = in.nextLine();
		if( response.trim().toLowerCase().equals( "indiv" ) || response.trim().length() == 0 ) {
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
				double[][] score = current.getScore();
				
				String outputFilename = filename.substring( 0, filename.length()-4 ) + ".out.csv";
				this.printer.print( score, results, outputFilename );
			}
		}
		else if( response.trim().toLowerCase().equals( "group" ) ) {
			System.out.print("output file name: ");
			response = in.nextLine();
			File outputFile = new File( response );
			if( !outputFile.exists() || !outputFile.isFile() ) {
				outputFile.createNewFile();
			}

			this.printer = new ResultPrinter( new File(".") );
			this.printer.printGroup( this.modelMap, outputFile );
		} else {
			throwException(response);
		}
	}

	private void throwException(String response) {
		throw new UnsupportedOperationException( response + " is invalid" );
	}
}
