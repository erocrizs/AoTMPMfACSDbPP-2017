import edu.mit.jwi.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.net.*;
import edu.mit.jwi.item.*;

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

		Log l = Parser.createLog("ADDU-SP02A-SP02B.in");
		//printLog(l);
	}

	public static void initializeDictionary() throws IOException {
		String path = "D:\\Nina\\Documents\\GitHub\\AoTMPMfACSDbPP-2017" + File.separator + "dict";
		URL url = new URL("file", null, path);

		//construct dictionary object and open it
		dict = new Dictionary(url);
		dict.open();
	}
	private static IDictionary dict = null;

	public static void testDictionary(String query) throws IOException {
		
		

		//look up first sense of the word "dog"
		IIndexWord idxWord = dict.getIndexWord(query, POS.ADJECTIVE);
		System.out.println(idxWord);
		System.out.println(idxWord.getWordIDs().size());
		IWordID wordID = idxWord.getWordIDs().get(0);
		for(IWordID iw: idxWord.getWordIDs()) {
			IWord w = dict.getWord(iw);
			List<IWord> wlist = w.getSynset().getWords();
			for(IWord w2: wlist) {
				System.out.print(w2.getLemma() + " ");
			}
			System.out.println();
		}

		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID);
		System.out.println("Lemma = " + word.getLemma());
		System.out.println("Gloss = " + word.getSynset().getGloss());
		
		List<IWordID> relWords = word.getRelatedWords();
		System.out.println("Related words: ");

		for(IWordID iw: relWords) {
			IWord w = dict.getWord(iw);
			System.out.print( w.getLemma() + " ");
		}
		System.out.println();

		System.out.println("Synonyms:");
		for(IWord iw: word.getSynset().getWords()) {
			System.out.print(iw.getLemma() + " ");
		}
		System.out.println();

	}

	public static List<IWord> getSynonyms(String query) {
		List<IWord> list = new ArrayList<IWord>();

		//could probably use the keyword-getter here!

		for(POS c: POS.values()) {
			
			//get IIndexWord for each POS
			IIndexWord idxWord = dict.getIndexWord(query, c);
			if(idxWord==null) continue;

			//get list of IWordIDs for the IIndexWord
			List<IWordID> wordIDs = idxWord.getWordIDs();

			for(IWordID iw: wordIDs) {
				IWord w = dict.getWord(iw);
				//list.add(w);

				// get synonyms
				List<IWord> synonyms = w.getSynset().getWords();
				for(IWord w2: synonyms) {
					list.add(w2);
				}
			}


		}

		return list;
	}

	public static void generateLinks(Utterance a, Utterance b) {
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
			Word aw = acontent.get(i);
			List<IWord> awsynonyms = aw.getSynonyms();

			for(int j = 0; j < bcontent.size(); j++) {
				Word bw = bcontent.get(j);
				List<IWord> bwsynonyms = bw.getSynonyms();

				if(isMatching(awsynonyms, bwsynonyms)) {
					implicitLinks.add(new Link(a.getId(), i, b.getId(), j));
				}
			}
		}
	}

	public static boolean isMatching(List<IWord> a, List<IWord> b) {
		for(IWord iwa: a) {
			for(IWord iwb: b) {
				if(iwa.getLemma().equals(iwb.getLemma())) return true;
			}
		}

		return false;
	}

	public static void attachSynonyms(Utterance u) {
		for(Word w: u.getContent()) {
			w.setSynonyms(getSynonyms(w.getContent()));
		}
	}
}