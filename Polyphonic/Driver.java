import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Driver {

	public static void main(String[] args) throws IOException {
		
		List<LogData> logData = new ArrayList<LogData>();
		List<ParticipantData> participantData = new ArrayList<ParticipantData>();
		List<UtteranceData> utteranceData = new ArrayList<UtteranceData>();
		
		File dataFolder = new File("data");
		KeywordDeriver deriver = new KeywordDeriver();
		SynonymFinder.initializeDictionary();
		for(File logFolder: dataFolder.listFiles()) {
			System.out.println( "Processing chat pair " + logFolder.getName() + "...");

			System.out.println("\tparsing the chat log...");
			Log log = Parser.createLog(new File(logFolder, "chat.in").getPath());

			System.out.println("\tderiving the keywords...");
			deriver.deriveKeywordFor(log);

			System.out.println("\tfinding implicit links...");
			SynonymFinder.deriveImplicitLinks(log);
			List<ImplicitLinkChain> chains = ImplicitLinkChain.getImplicitLinkChains(log);

			System.out.println("\tcomputing utterance strengths and participang contributions...");
			ContributionCounter cc = new ContributionCounter(log, chains);

			System.out.println("\tparsing the speech acts and interanimation pattern...");
			IAPFinder.parseOutputXML( log, new File(logFolder, "speech-acts.xml").getPath() );

			System.out.println("\tfinalizing pair data...");
			File spdAccFolder = new File(logFolder, "speed-accuracy");
			logData.add( log.getDate(spdAccFolder, cc) );

			System.out.println("\tfinalizing participang data...");
			for(Participant p: log.getParticipants()) {
				participantData.add( p.getData(spdAccFolder, cc) );
			}

			System.out.println("\tfinalizing participant data...");
			for(Utterance u: log.getUtterances()) {
				utteranceData.add( u.getData(cc) );
			}
			
			System.out.println();
			System.out.println( "\tUtterance count: " + log.getUtterances().size() );
			System.out.println( "\tFinished with " + logFolder.getName() );
			System.out.println();
		}
		
		System.out.println("log count: " + logData.size());
		System.out.println("part. count: " + participantData.size());
		System.out.println("utter. count: " + utteranceData.size());
		System.out.println();
		
		System.out.println("Printing the xml...");
		XMLGgobiPrinter.printGgobiXML( new File("polyphony.xml"), logData, participantData, utteranceData );
		System.out.println("\tfinished writing");
		
		System.out.println();
		System.out.println("Done!");
	}

}
