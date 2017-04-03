import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Driver {

	public static void main(String[] args) throws IOException {
		Log log = Parser.createLog("ADDU-SP02A-SP02B.in");
		KeywordDeriver deriver = new KeywordDeriver();
		deriver.deriveKeywordFor(log);
		SynonymFinder.deriveImplicitLinks(log);
		ArrayList<Utterance> utters = log.getUtterances();
		List<ImplicitLinkChain> chains = ImplicitLinkChain.getImplicitLinkChains(log);
		ContributionCounter cc = new ContributionCounter(log, chains);
		
		for(Utterance utter: utters) {
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
	}

}
