import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Driver {

	public static void main(String[] args) throws IOException {
		Log log = Parser.createLog("ADDU-SP02A-SP02B.in");
		KeywordDeriver deriver = new KeywordDeriver();
		deriver.deriveKeywordFor(log);
		SynonymFinder.deriveImplicitLinks(log);
		ArrayList<Utterance> utters = log.getUtterances();
		List<ImplicitLinkChain> chains = ImplicitLinkChain.getImplicitLinkChains(log);
		
		for(ImplicitLinkChain ch: chains) {
			System.out.printf("Chain for '%s' starting at utterance %d:\n", ch.getTopic().getContent(), ch.getHeadIndex());
			for(int i: ch.getLinkIndices()) {
				Utterance current = utters.get(i);
				System.out.printf("- ( id: %d ) %s: %s\n", current.getId(),  current.getSpeaker().getCodeName(), current.getContentString());
			}
		}
	}

}
