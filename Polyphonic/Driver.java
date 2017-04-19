import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Driver {

	public static void main(String[] args) throws IOException {
		/*	
		Log log = Parser.createLog("chat.in");
		KeywordDeriver deriver = new KeywordDeriver();
		deriver.deriveKeywordFor(log);
		SynonymFinder.deriveImplicitLinks(log);
		ArrayList<Utterance> utters = log.getUtterances();
		List<ImplicitLinkChain> chains = ImplicitLinkChain.getImplicitLinkChains(log);
		ContributionCounter cc = new ContributionCounter(log, chains);
		IAPFinder.parseOutputXML( log, "speech-acts.xml" );
		
		for(int i=0; i<log.getUtterances().size(); i++) {
			System.out.println( i + ") " + log.getUtterances().get(i).getContentString() );
			System.out.println( "Pattern: " + IAPFinder.whatPattern(log, i) );
			//System.out.println("SP: " + log.getUtterances().get(i).getSpeechActs().size() );
			
			List<String> speechActs = log.getUtterances().get(i).getSpeechActs();
			System.out.print("SP:");
			if(speechActs != null) {
				for( String sp: speechActs ) {
					System.out.print(" " + sp);
				}
			}
			System.out.println("\n");
			
		}
		*/
		List<LogData> logData = new ArrayList<LogData>();
		List<ParticipantData> participantData = new ArrayList<ParticipantData>();
		List<UtteranceData> utteranceData = new ArrayList<UtteranceData>();
		
		File dataFolder = new File("data");
		KeywordDeriver deriver = new KeywordDeriver();
		SynonymFinder.initializeDictionary();
		for(File logFolder: dataFolder.listFiles()) {
			Log log = Parser.createLog(new File(logFolder, "chat.in").getPath());
			deriver.deriveKeywordFor(log);
			SynonymFinder.deriveImplicitLinks(log);
			List<ImplicitLinkChain> chains = ImplicitLinkChain.getImplicitLinkChains(log);
			ContributionCounter cc = new ContributionCounter(log, chains);
			IAPFinder.parseOutputXML( log, new File(logFolder, "speech-acts.xml").getPath() );
			
			File spdAccFolder = new File(logFolder, "speed-accuracy");
			logData.add( log.getDate(spdAccFolder, cc) );
			
			for(Participant p: log.getParticipants()) {
				participantData.add( p.getData(spdAccFolder, cc) );
			}
			
			for(Utterance u: log.getUtterances()) {
				utteranceData.add( u.getData(cc) );
			}
			
			System.out.println( "Finished with " + logFolder.getName() );
		}
		
		System.out.println("log count: " + logData.size());
		System.out.println("part. count: " + participantData.size());
		System.out.println("utter. count: " + utteranceData.size());
		
		/*for(Utterance utter: utters) {
			System.out.printf(
					"%d) %s\n%s\nStrength: %d\n", 
					utter.getId(), 
					utter.getSpeaker().
					getCodeName(), 
					utter.getContentString(), 
					cc.getUtteranceStrength(utter));
			System.out.print("Links to:");
			int id = utter.getId();
			Collections.sort( utter.getImplicitLinks(), (a, b) -> Integer.compare(a.getUtteranceIdB(), b.getUtteranceIdB()) );
			for(Link l: utter.getImplicitLinks()) {
				int next = l.getUtteranceIdB();
				
				if(next - id > 5) break; 
				System.out.print(" " + next);
			}
			System.out.println("\n");
		}
		
		for(Participant p: cc.getParticipantList()) {
			System.out.println( "contribution (" + p.getCodeName() + ") : " + cc.getContribution( p ) );
		}
		*/
		
		
	}

}
