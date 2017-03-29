import edu.mit.jwi.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.*;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.*;

public class SynonymFinder {
	public static void main(String[] args) throws IOException {
		
		initializeDictionary();
		//testDictionary();
		Log l = Parser.createLog("ADDU-SP02A-SP02B.in");
		KeywordDeriver keywordDeriver = new KeywordDeriver();
		keywordDeriver.deriveKeywordFor(l);
		
		ArrayList<Utterance> list = l.getUtterances();
		
		System.out.println("[0] Utterance pairs");
		System.out.println("[1] All utterances");
		System.out.println();
		
		Scanner sc = new Scanner(System.in);
		
		int mode = sc.nextInt();

		if(mode==0) {
			while(sc.hasNextLine()) {
				int u1 = sc.nextInt();
				int u2 = sc.nextInt();

				Utterance ut1 = list.get(u1);
				Utterance ut2 = list.get(u2);

				ArrayList<Link> links = generateLinks(ut1, ut2);
				processLinks(ut1, ut2, links, true);
				System.out.println();
			}
		}
		else if(mode==1) {
			for(int i = 0; i < list.size(); i++) {
				for(int j = i+1; j < list.size(); j++) {
					Utterance ut1 = list.get(i);
					Utterance ut2 = list.get(j);

					ArrayList<Link> links = generateLinks(ut1, ut2);
					processLinks(ut1, ut2, links, false);
				}
			}
		}
	}
	
	public static void deriveImplicitLinks(Log l) throws IOException {
		initializeDictionary();
		List<Utterance> utteranceList = l.getUtterances();
		for(int i = 0; i < utteranceList.size(); i++) {
			Utterance ut1 = utteranceList.get(i);
			ArrayList<Link> ut1Link = new ArrayList<Link>();
			for(int j = i+1; j < utteranceList.size(); j++) {
				Utterance ut2 = utteranceList.get(j);

				ArrayList<Link> links = generateLinks(ut1, ut2);
				ut1Link.addAll( links );
			}
			ut1Link.sort( (a, b) -> Integer.compare( a.getUtteranceIdB(), b.getUtteranceIdB() ) );
			ut1.setImplicitLinks( ut1Link );
		}
	}

	public static void testDictionary () throws IOException {

		// look up first sense of the word "dog "
		IIndexWord idxWord = dict.getIndexWord ("representation", POS.NOUN );
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID );
		System.out.println(" Lemma = " + word.getLemma());
		System.out.println(" Gloss = " + word.getSynset().getGloss());
		System.out.println("Related words = ");
		for(IWordID iwid: word.getRelatedWords()) {
			System.out.println(dict.getWord(iwid).getLemma());
		}
	}

	public static void initializeDictionary() throws IOException {
		String path = "dict";
		URL url = new URL("file", null, path);

		//construct dictionary object and open it
		dict = new Dictionary(url);
		dict.open();
	}
	private static IDictionary dict = null;

	public static List<String> getSynonyms(String query) {
		List<String> list = new ArrayList<String>();

		//could probably use the keyword-getter here!
		//System.out.println(query);
		WordnetStemmer stemmer = new WordnetStemmer(dict);

		List<String> test = stemmer.findStems(query, POS.NOUN);

		for(String str: test) {
			IIndexWord idxWord = null;
			try {
				idxWord = dict.getIndexWord(str, POS.NOUN);
			} catch(NullPointerException e) { return null; }
			if(idxWord==null) return null;

			//System.out.println("String: " + str);
			//System.out.println("	success!");

			//get list of IWordIDs for the IIndexWord
			List<IWordID> wordIDs = idxWord.getWordIDs();

			for(IWordID iw: wordIDs) {
				IWord w = dict.getWord(iw);
				//list.add(w);

				// get synonyms
				List<IWord> synonyms = w.getSynset().getWords();
				for(IWord w2: synonyms) {
					list.add(w2.getLemma().replace('_', ' '));
					//System.out.print(w2.getLemma() + " ");
				}
				//System.out.println("\n");
			}
		}

		return list;
	}

	public static ArrayList<Link> generateLinks(Utterance a, Utterance b) {
		if(!a.processedSynonyms()) {
			attachSynonyms(a);
			a.setProcessedSynonyms(true);
		}
		if(!b.processedSynonyms()) {
			attachSynonyms(b);
			b.setProcessedSynonyms(true);
		}

		ArrayList<Link> implicitLinks = new ArrayList<Link>();

		List<Word> acontent = a.getTopics();
		List<Word> bcontent = b.getTopics();
		
		for(int i = 0; i < acontent.size(); i++) {
			Word aw = acontent.get(i);
			List<String> awsynonyms = aw.getSynonyms();

			for(int j = 0; j < bcontent.size(); j++) {
				Word bw = bcontent.get(j);
				List<String> bwsynonyms = bw.getSynonyms();

				if(awsynonyms.contains( bw.getContent() ) ||
					bwsynonyms.contains( aw.getContent() ) ||
					aw.equals(bw) ) {
					implicitLinks.add(new Link(a.getId(), aw.getIndex(), b.getId(), bw.getIndex()));
				}
			}
		}

		return implicitLinks;
	}

	public static boolean isMatching(List<String> a, List<String> b) {

		if(a==null || b==null) return false;
		for(String iwa: a) {
			for(String iwb: b) {
				if(iwa.equals(iwb)) return true;
			}
		}

		return false;
	}

	public static void attachSynonyms(Utterance u) {
		for(Word w: u.getNounLemmaList()) {
			w.setSynonyms(getSynonyms(w.getContent()));
		}
		for(Word w: u.getProperNounList()) {
			w.setSynonyms(getSynonyms(w.getContent()));
		}
	}

	public static void processLinks(Utterance u1, Utterance u2, ArrayList<Link> list, boolean showNull) {
		boolean isNull = list.size()==0;

		if(isNull && !showNull) {
			return;
		}

		System.out.printf("Utterance %d: \"%s\"\n", u1.getId(), u1.getContentString().trim());
		System.out.printf("Utterance %d: \"%s\"\n", u2.getId(), u2.getContentString().trim());

		ArrayList<Word> content1 = u1.getContent();
		ArrayList<Word> content2 = u2.getContent();

		for(Link x: list) {
			int ind1 = x.getWordIndexA();
			int ind2 = x.getWordIndexB();
			System.out.printf("Index %d word \"%s\" matches with Index %d word \"%s\"\n", ind1, content1.get(ind1).getContent(), ind2, content2.get(ind2).getContent());
		}
		System.out.println();
	}
}