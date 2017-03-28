import edu.mit.jwi.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.net.*;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.*;

public class SynonymFinder {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		/*while(sc.hasNextLine()) {
			initializeDictionary();
			String query = sc.nextLine();

			for(IWord w: getSynonyms(query)) {
				System.out.println(w.getLemma());
			}
			//testDictionary(sc.nextLine());
		}*/

		initializeDictionary();
		//testDictionary();
		Log l = Parser.createLog("ADDU-SP02A-SP02B.in");
		ArrayList<Utterance> list = l.getUtterances();
		while(sc.hasNextLine()) {
			int u1 = sc.nextInt();
			int u2 = sc.nextInt();

			Utterance ut1 = list.get(u1);
			Utterance ut2 = list.get(u2);

			ArrayList<Link> links = generateLinks(ut1, ut2);
			processLinks(ut1, ut2, links);
			System.out.println();
		}

		//printLog(l);

		/*Utterance u = list.get(15);
		attachSynonyms(u);
		for(Word w: u.getContent()) {
			System.out.print("word: " + w.getContent() + " - ");
			for(IWord iw: w.getSynonyms()) {
				System.out.print(iw.getLemma() + " ");
			}
			System.out.println();
		}*/
	}

	public static void testDictionary () throws IOException {

		// look up first sense of the word "dog "
		IIndexWord idxWord = dict.getIndexWord ("problems", POS.NOUN );
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID );
		System.out.println(" Lemma = " + word.getLemma());
		System.out.println(" Gloss = " + word.getSynset().getGloss());
	}

	public static void initializeDictionary() throws IOException {
		String path = "D:\\Nina\\Documents\\GitHub\\AoTMPMfACSDbPP-2017" + File.separator + "dict";
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

		List<Word> acontent = a.getContent();
		List<Word> bcontent = b.getContent();

		for(int i = 0; i < acontent.size(); i++) {
			//System.out.println("here1");
			Word aw = acontent.get(i);
			List<String> awsynonyms = aw.getSynonyms();

			for(int j = 0; j < bcontent.size(); j++) {
				//System.out.println("here2");
				Word bw = bcontent.get(j);
				List<String> bwsynonyms = bw.getSynonyms();

				if(isMatching(awsynonyms, bwsynonyms)) {
					//System.out.println("here3");
					implicitLinks.add(new Link(a.getId(), i, b.getId(), j));
				}
			}
		}

		return implicitLinks;
	}

	public static boolean isMatching(List<String> a, List<String> b) {
		//System.out.println(a.size() + " " + b.size());

		if(a==null || b==null) return false;
		for(String iwa: a) {
			for(String iwb: b) {
				//System.out.println(iwa.getLemma() + " " + iwb.getLemma());
				if(iwa.equals(iwb)) return true;
			}
		}

		return false;
	}

	public static void attachSynonyms(Utterance u) {
		for(Word w: u.getContent()) {
			w.setSynonyms(getSynonyms(w.getContent()));
		}
	}

	public static void processLinks(Utterance u1, Utterance u2, ArrayList<Link> list) {
		System.out.printf("Utterance %d: \"%s\"\n", u1.getId(), u1.getContentString());
		System.out.printf("Utterance %d: \"%s\"\n", u2.getId(), u2.getContentString());

		ArrayList<Word> content1 = u1.getContent();
		ArrayList<Word> content2 = u2.getContent();

		for(Link x: list) {
			int ind1 = x.getWordIndexA();
			int ind2 = x.getWordIndexB();
			System.out.printf("Index %d word \"%s\" matches with Index %d word \"%s\"\n", ind1, content1.get(ind1).getContent(), ind2, content2.get(ind2).getContent());
		}
	}
}