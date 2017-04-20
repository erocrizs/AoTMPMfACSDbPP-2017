import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLGgobiPrinter {
	private static String[] partA = new String[] {
			"<?xml version=\"1.0\"?>",
			"<!DOCTYPE ggobidata SYSTEM \"ggobi.dtd\">",
			"<ggobidata count=\"3\">",
			"\t<data name=\"per pair\">",
			"\t\t<description>",
			"\t\t\tPairs",
			"\t\t</description>",
			"\t\t<variables count=\"5\">",
			"\t\t\t<integervariable name=\"utterance\" nickname=\"ut\" />",
			"\t\t\t<realvariable name=\"score\" nickname=\"sc\" />",
			"\t\t\t<realvariable name=\"unitive pattern\" nickname=\"pu\" />",
			"\t\t\t<realvariable name=\"difference pattern\" nickname=\"pd\" />",
			"\t\t\t<realvariable name=\"code-switched\" nickname=\"cs\" />",
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
			"\t\t<variables count=\"5\">",
			"\t\t\t<integervariable name=\"speed\" nickname=\"sp\" />",
			"\t\t\t<realvariable name=\"score\" nickname=\"sc\" />",
			"\t\t\t<integervariable name=\"unitive pattern\" nickname=\"pu\" />",
			"\t\t\t<integervariable name=\"difference pattern\" nickname=\"pd\" />",
			"\t\t\t<integervariable name=\"contribution\" nickname=\"cn\" />",
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
			bw.write("\t\t\t<record glyph=\"plus 4\">\n");
			String toWrite = "\t\t\t\t";
			toWrite += ld.utterance + " ";
			toWrite += ld.averageAccuracy + " ";
			toWrite += ld.unitiveUtteranceCount + " ";
			toWrite += ld.differenceUtteranceCount + " ";
			toWrite += ld.codeSwitchedCount + "\n";
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
			bw.write("\t\t\t<record glyph=\"plus 4\">\n");
			String toWrite = "\t\t\t\t";
			toWrite += pd.speed + " ";
			toWrite += pd.accuracy + " ";
			toWrite += pd.unitiveUtteranceCount + " ";
			toWrite += pd.differenceUtteranceCount + " ";
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
			bw.write("\t\t\t<record glyph=\"plus 4\">\n");
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
