import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLGgobiPrinter {
	private static String[] partA = new String[] {
			"<?xml version=\"1.0\"?>",
			"<!DOCTYPE ggobidata SYSTEM \"ggobi.dtd\">",
			"<ggobidata count=\"1\">",
			"\t<data name=\"per pair\">",
			"\t\t<description>",
			"\t\t\tPairs",
			"\t\t</description>",
			"\t\t<variables count=\"8\">",
			"\t\t\t<integervariable name=\"utterance\" nickname=\"ut\" />",
			"\t\t\t<realvariable name=\"accuracy\" nickname=\"ac\" />",
			"\t\t\t<integervariable name=\"unitive pattern count\" nickname=\"upc\" />",
			"\t\t\t<integervariable name=\"difference pattern count\" nickname=\"dpc\" />",
			"\t\t\t<integervariable name=\"code-switched count\" nickname=\"csc\" />",
			"\t\t\t<realvariable name=\"unitive pattern percentage\" nickname=\"upp\" />",
			"\t\t\t<realvariable name=\"difference pattern percentage\" nickname=\"dpp\" />",
			"\t\t\t<realvariable name=\"code-switched percentage\" nickname=\"csp\" />",
			"\t\t</variables>",
			"\t\t<records count=\"2017\">"
	};
	private static String[] partB = new String[] {
			"\t\t</records>",
			"\t</data>",
			"\t<data name=\"per user\">",
			"\t\t<description>",
			"\t\t\tUsers",
			"\t\t</description>",
			"\t\t<variables count=\"10\">",
			"\t\t\t<integervariable name=\"time\" nickname=\"t\" />",
			"\t\t\t<realvariable name=\"accuracy\" nickname=\"acc\" />",
			"\t\t\t<integervariable name=\"utterance count\" nickname=\"u\" />",
			"\t\t\t<integervariable name=\"unitive pattern count\" nickname=\"upc\" />",
			"\t\t\t<integervariable name=\"difference pattern count\" nickname=\"dpc\" />",
			"\t\t\t<integervariable name=\"code-switched count\" nickname=\"csc\" />",
			"\t\t\t<realvariable name=\"unitive pattern percentage\" nickname=\"upp\" />",
			"\t\t\t<realvariable name=\"difference pattern percentage\" nickname=\"dpp\" />",
			"\t\t\t<realvariable name=\"code-switched percentage\" nickname=\"csp\" />",
			"\t\t\t<integervariable name=\"contribution\" nickname=\"c\" />",
			"\t\t</variables>",
			"\t\t<records count=\"2017\">"
	};
	private static String[] partC = new String[] {
			"\t\t</records>",
			"\t</data>",
			"\t<data name=\"per utterance\">",
			"\t\t<description>",
			"\t\t\tUtterances",
			"\t\t</description>",
			"\t\t<variables count=\"3\">",
			"\t\t\t<integervariable name=\"strength\" nickname=\"st\" />",
			"\t\t\t<categoricalvariable name=\"code-switched\" nickname=\"cs\" >",
			"\t\t\t\t<levels count=\"2\">",
			"\t\t\t\t\t<level value=\"1\">Yes</level>",
			"\t\t\t\t\t<level value=\"2\">No</level>",
			"\t\t\t\t</levels>",
			"\t\t\t</categoricalvariable>",
			"\t\t\t<categoricalvariable name=\"interanimation pattern\" nickname=\"ip\" >",
			"\t\t\t\t<levels count=\"4\">",
			"\t\t\t\t\t<level value=\"1\">Unitive</level>",
			"\t\t\t\t\t<level value=\"2\">Difference</level>",
			"\t\t\t\t\t<level value=\"3\">None</level>",
			"\t\t\t\t\t<level value=\"4\">Both</level>",
			"\t\t\t\t</levels>",
			"\t\t\t</categoricalvariable>",
			"\t\t</variables>",
			"\t\t<records count=\"2017\">"
	};
	private static String[] partD = new String[] {
			"\t\t</records>",
			"\t</data>",
			"</ggobidata>"
	};
	
	public static void printGgobiXML(File f, List<LogData> logData, List<ParticipantData> partData, List<UtteranceData> uttData) throws IOException {
		if(!f.exists()) {
			f.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter( f ));
		
		for(int i=0; i<partA.length; i++) {
			if(i == partA.length-1) {
				String toWrite = partA[i].replace( "2017", Integer.toString( logData.size() ) );
				bw.write( toWrite + "\n" );
			} else {
				bw.write( partA[i] + "\n" );
			}
		}
		
		for(LogData ld: logData) {
			bw.write("\t\t\t<record color=\"" + ld.color + "\" glyphType=\"5\" glyphSize=\"2\" label=\"" + ld.name + "\" >\n");
			String toWrite = "\t\t\t\t";
			toWrite += ld.utterance + " ";
			toWrite += ld.averageAccuracy + " ";
			toWrite += ld.unitiveUtteranceCount + " ";
			toWrite += ld.differenceUtteranceCount + " ";
			toWrite += ld.codeSwitchedCount + " ";
			toWrite += ld.unitiveUtterancePercentage + " ";
			toWrite += ld.differenceUtterancePercentage + " ";
			toWrite += ld.codeSwitchedPercentage + "\n";
			bw.write( toWrite );
			bw.write("\t\t\t</record>\n");
		}
		
		for(int i=0; i<partB.length; i++) {
			if(i == partB.length-1) {
				String toWrite = partB[i].replace( "2017", Integer.toString( partData.size() ) );
				bw.write( toWrite + "\n" );
			} else {
				bw.write( partB[i] + "\n" );
			}
		}
		
		for(ParticipantData pd: partData) {
			bw.write("\t\t\t<record color=\"" + pd.color + "\" glyphType=\"" + pd.glyph + "\" glyphSize=\"4\" label=\"" + pd.name + "\" >\n");
			String toWrite = "\t\t\t\t";
			toWrite += pd.speed + " ";
			toWrite += pd.accuracy + " ";
			toWrite += pd.utterance + " ";
			toWrite += pd.unitiveUtteranceCount + " ";
			toWrite += pd.differenceUtteranceCount + " ";
			toWrite += pd.codeSwitchedCount + " ";
			toWrite += pd.unitiveUtterancePercentage + " ";
			toWrite += pd.differenceUtterancePercentage + " ";
			toWrite += pd.codeSwitchedPercentage + "\n";
			toWrite += pd.contribution + "\n";
			bw.write( toWrite );
			bw.write("\t\t\t</record>\n");
		}
		
		for(int i=0; i<partC.length; i++) {
			if(i == partC.length-1) {
				String toWrite = partC[i].replace( "2017", Integer.toString( uttData.size() ) );
				bw.write( toWrite + "\n" );
			} else {
				bw.write( partC[i] + "\n" );
			}
		}
		
		for(UtteranceData ud: uttData) {
			bw.write("\t\t\t<record glyph=\"plus 4\" label=\"" + ud.name + "\" >\n");
			String toWrite = "\t\t\t\t";
			toWrite += ud.strength + " ";
			toWrite += ud.isCodeSwitched + " ";
			toWrite += ud.pattern + "\n";
			bw.write( toWrite );
			bw.write("\t\t\t</record>\n");
		}
		
		for(int i=0; i<partD.length; i++) {
			bw.write( partD[i] + "\n" );
		}
		
		bw.close();
	}
}
