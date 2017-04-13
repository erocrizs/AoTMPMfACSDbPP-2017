import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class ClauseDivider {
	private static final String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private static TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
	private static LexicalizedParser parser = LexicalizedParser.loadModel( PCG_MODEL );
	
	private static final String CLAUSE_TAGS= "S SBAR SBARQ SINV SQ";
	
	private static ArrayList<String> temp = new ArrayList<String>();
	private static ArrayList<String> clauseList = new ArrayList<String>();
	
	private static Tree getTree(String sentence) {
		List<CoreLabel> tokens = tokenizerFactory.getTokenizer(new StringReader( sentence ) ).tokenize();
    	Tree tree = parser.apply(tokens);
    	return tree;
	}
	
	private static boolean isClausal(Tree t) {
		if( t.isEmpty() || t.isLeaf() ) return false;
		String head = t.value();
		boolean root = "ROOT".equals( head );
		boolean clause = CLAUSE_TAGS.contains( head );
		return (root || clause);
	}
	
	// get all sub clauses and place them in clauseList
	// returns true if given subtree is a clause
	private static void processTree(Tree t) {
		if( t.isLeaf() ) {
			temp.add( t.label().value() );
			return;
		}
		
		
		List<Tree> children = t.getChildrenAsList();
		boolean isS = !t.isLeaf() && "S".equals( t.value() );
		boolean hasNP = false;
		boolean hasVP = false;
		
		for(int i=children.size()-1; i>=0; i--) {
			Tree curr = children.get(i);
			processTree( curr );
			
			if( !curr.isLeaf() ) {
				hasNP = hasNP || "NP".equals( curr.value() );
				hasVP = hasVP || "VP".equals( curr.value() );
			}
		}
		
		if( isS && hasNP && hasVP ) {
			String s = getTemp();
			temp.clear();
			clauseList.add( s );
		}
		
	}
	
	private static String getTemp() {
		String s = "";
		for(int i=temp.size()-1; i>=0; i--) {
			String toAdd = temp.get(i);
			if( ".,;?!".contains(toAdd) ) {
				s += toAdd;
			} else {
				s += " " + toAdd;
			}
		}
		temp.clear();
		return s.trim();
	}
	
	public static String[] getClauses(String sentence) {
		processTree( getTree(sentence) );
		if( temp.size() > 0 ) {
			clauseList.add( getTemp() );
		}
		
		String[] clauses = new String[ clauseList.size() ];
		int n = clauseList.size();
		for(int i=0; i<n; i++) {
			clauses[i] = clauseList.get( n-1-i );
		}
		
		clauseList.clear();
		return clauses;
	}
	
	public static void main(String[] args) {
		String[] samples = new String[] {
				"She did not cheat on the test, for it was not the right thing to do.",
				"I think I will buy the red car, or I will lease the blue one.",
				"I really want to go to work, but I am too sick to drive.",
				"I am counting my calories, yet I really want dessert.",
				"He ran out of money, so he had to stop playing poker.",
				"They got there early, and they got really good seats.",
				"There was no ice cream in the freezer, nor did they have money to go to the store.",
				"Everyone was busy, so I went to the movie alone.",
				"I would have gotten the promotion, but my attendance wasn’t good enough.",
				"Should we start class now, or should we wait for everyone to get here?",
				"It was getting dark, and we weren’t there yet.",
				"Cats are good pets, for they are clean and are not noisy.",
				"We have never been to Asia, nor have we visited Africa.",
				"He didn’t want to go to the dentist, yet he went anyway.",
				"I already proceeded to the next problem, I've got 2 errors too, but we've got different error locations."
		};
		
		
		for(String sample: samples) {
			String[] stuff = getClauses( sample );
			for(String x: stuff) {
				System.out.println("xx) " + x );
			}
			System.out.println();
		}
		System.out.println("SS");
	}
}
