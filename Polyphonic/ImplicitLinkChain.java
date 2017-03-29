import java.util.ArrayList;
import java.util.List;

public class ImplicitLinkChain {
	private Word topic;
	private int head;
	private Utterance headUtterance;
	private int wordIndex;
	private ArrayList<Integer> links;
	
	private static int DISTANCE_TOLERANCE = 5;
	
	public ImplicitLinkChain(int head, int wordIndex, Log log ) {
		this.head = head;
		this.wordIndex = wordIndex;
		this.headUtterance = log.getUtterances().get( head );
		this.topic = log.getUtterances().get( head )
						.getContent().get( wordIndex );
		this.links = new ArrayList<Integer>();
		this.links.add( head );
		this.findImplicitChain( );
	}
	
	private void findImplicitChain( ) {
		int lastLink = head;
		//System.out.println("XXXX " + head);
		for( Link l: this.headUtterance.getImplicitLinks() ) {
			//System.out.println(l.getUtteranceIdA() + " " + l.getUtteranceIdB());
			if( l.getUtteranceIdB() - lastLink > DISTANCE_TOLERANCE ) break;
			if( l.getWordIndexA() == this.wordIndex ) {
				this.links.add( l.getUtteranceIdB() );
				lastLink = l.getUtteranceIdB();
			}
		}
	}
	
	public int size() {
		return this.links.size();
	}
	
	public Word getTopic() {
		return topic;
	}
	
	public int getHeadIndex() {
		return head;
	}
	
	public List<Integer> getLinkIndices() {
		return this.links;
	}
	
	public static List<ImplicitLinkChain> getImplicitLinkChains(Log log) {
		ArrayList<Utterance> utters = log.getUtterances();
		
		ArrayList<ImplicitLinkChain> chains = new ArrayList<ImplicitLinkChain>();
		for(int i=0; i<utters.size(); i++) {
			Utterance head = utters.get(i);
			for(Word topic: head.getTopics()) {
				int index = topic.getIndex();
				ImplicitLinkChain temp = new ImplicitLinkChain(i, index, log);
				if(temp.size() >= 2) {
					chains.add( temp );
				}
			}
		}
		
		return chains;
	}
}
