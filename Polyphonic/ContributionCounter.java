import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContributionCounter {
	
	private class UtteranceNode {
		int id;
		int value;
		Utterance utter;
		ArrayList<UtteranceNode> refferant;
		
		public UtteranceNode(Utterance utter) {
			this.utter = utter;
			this.refferant = new ArrayList<UtteranceNode>();
			this.value = 1;
		}
	}
	
	private ArrayList<UtteranceNode> utteranceNodes;
	private HashMap<Participant, Integer> contribution;
	private ArrayList<Participant> participantList;
	
	public ContributionCounter(Log log, List<ImplicitLinkChain> chains) {
		this.utteranceNodes = new ArrayList<UtteranceNode>();
		this.contribution = new HashMap<Participant, Integer>();
		this.participantList = new ArrayList<Participant>();
		
		for(Utterance u: log.getUtterances()) {
			utteranceNodes.add( new UtteranceNode( u ) );
		}
		
		for(ImplicitLinkChain c: chains) {
			List<Integer> chain = c.getLinkIndices();
			int length = chain.size();
			
			int last = chain.get(0);
			for(int i=1; i<length; i++) {
				UtteranceNode referrant = utteranceNodes.get(last);
				last = chain.get(i);
				UtteranceNode referred = utteranceNodes.get(last);
				referred.refferant.add( referrant );
			}
		}
		
		for(int i=utteranceNodes.size()-1; i>=0; i--) {
			UtteranceNode current = utteranceNodes.get(i);
			Participant speaker = current.utter.getSpeaker();
			int value = current.value;
			
			for(UtteranceNode un: current.refferant) {
				un.value += value;
			}
			
			if( !contribution.containsKey(speaker) ) {
				contribution.put(speaker, value);
				participantList.add( speaker );
			} else {
				int oldValue = contribution.get(speaker);
				contribution.put(speaker, value + oldValue );
			}
		}
	}
	
	public List<Participant> getParticipantList() {
		return this.participantList;
	}
	
	public int getContribution(Participant s) {
		return this.contribution.get( s );
	}
	
	public UtteranceNode getUtteranceNode(int i) {
		return this.utteranceNodes.get(i);
	}
	
	public int getUtteranceStrength( Utterance utter ) {
		int id = utter.getId();
		UtteranceNode un = utteranceNodes.get( id );
		if( id != un.utter.getId() ) {
			throw new UnsupportedOperationException();
		}
		return un.value;
	}
}
