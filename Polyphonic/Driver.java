
public class Driver {

	public static void main(String[] args) {
		Log log = Parser.createLog("ADDU-SP02A-SP02B.in");
		KeywordDeriver deriver = new KeywordDeriver();
		deriver.deriveKeywordFor(log);
		
		for(Utterance u: log.getUtterances()) {
			Participant sp = u.getSpeaker();
			System.out.printf("id: %d \t speaker: %s %s\n", u.getId(), sp.getCodeName(), sp.getRealName());
			System.out.println("code-switched? " + u.isCodeSwitched() + "\t time: " + u.getTime().toString());
			System.out.println(u.getContentString());
			System.out.print("Noun Topics: ");
			for( Word lemma: u.getNounLemmaList() ) {
				System.out.print( lemma.getContent() + ", " );
			}
			System.out.println("<<end>>");
			System.out.print("NE Topics: ");
			for( Word lemma: u.getProperNounList() ) {
				System.out.print( lemma.getContent() + ", " );
			}
			System.out.println("<<end>>");

			System.out.println();
		}
		System.out.println("DONE");
	}

}
