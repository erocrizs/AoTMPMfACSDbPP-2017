import java.io.*;
import java.util.*;

public class IAPFinder {

	public static void main(String[] args) throws IOException {
		Log log = Parser.createLog("ADDU-SP02A-SP02B.in");
		KeywordDeriver deriver = new KeywordDeriver();
		deriver.deriveKeywordFor(log);
		SynonymFinder.deriveImplicitLinks(log);
		ArrayList<Utterance> utters = log.getUtterances();
		List<ImplicitLinkChain> chains = ImplicitLinkChain.getImplicitLinkChains(log);
		ContributionCounter cc = new ContributionCounter(log, chains);
		
		//IAPFinder.convertToInputXML(log);
		IAPFinder.parseOutputXML( log, "output.xml" );
		
		//Mic
		Participant p1 = log.getUtterances().get(32).getSpeaker();
		//Jo
		Participant p2 = log.getUtterances().get( 33 ).getSpeaker();
		System.out.println( "Mic: " + p1 + " Jo: " + p2 );
		
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String str = sc.next();
			int[] arr;
			if(str.equals( "Mic" ))
				arr= howManyUtterances(log, p1);
			else
				arr = howManyUtterances(log, p2);
			
			System.out.println( arr[0] + " " + arr[1] );
		}
	}

	public static void convertToInputXML(Log l) {
		System.out.println("Beginning");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("input.xml"));
		} catch(FileNotFoundException e) { return; }
		
		pw.println("<?xml version=\"1.0\"?>");
		pw.println("<dialogue corpus=\"trainline\" id=\"01\" lang=\"en\">\n");
		
		ArrayList<Utterance> utters = l.getUtterances();
		
		for(Utterance u: utters) {
			int id = u.getId();
			String speaker = u.getSpeaker().getRealName();
			String content = u.getContentString().toLowerCase();
			ArrayList<Word> pronouns = u.getProperNounList();
			ArrayList<Word> utterWords = u.getContent();
			
			content = content.replaceAll( "&", "&amp;" );
			content = content.replaceAll( "\"", "&quot;" );
			content = content.replaceAll( "<", "&lt;" );
			content = content.replaceAll( ">", "&gt;" );
			
			//System.out.println( "\"" + content + "\"" );
			char endChar = content.charAt( content.length()-2 );
			//System.out.println(id + " " + endChar);
			if( endChar != '.' && endChar != '!' && endChar != '?') {
				content = content + " <punc type=\"stop\" /> ";
			}
			
			content = content.replaceAll("[.,]", " <punc type=\"stop\" /> ");
			content = content.replaceAll("[!]", " <punc type=\"exclam\" /> ");
			content = content.replaceAll("[?]", " <punc type=\"query\" /> ");
			
			pw.println("<turn n=\"" + (id+1) + "\" speaker=\"" + speaker + "\">");
			pw.println(content);
			pw.println("</turn>\n");
			
		}
		
		pw.println("\n</dialogue>");
		pw.close();
		System.out.println("Finished");
	}
	
	public static void parseOutputXML(Log l, String fileName) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) { return; }
		
		ArrayList<Utterance> utters = l.getUtterances();
		
		while(sc.hasNextLine()) {
			String turn = "";
			String incomingLine = "";
			
			while(sc.hasNextLine()) {
				incomingLine = sc.nextLine();
				if(incomingLine.contains( "<turn" ))
					break;
			}
			
			if(!sc.hasNextLine()) break;
			
			int id = extractIdNumber(incomingLine)-1;
			
			turn += incomingLine;
			
			while(sc.hasNextLine()) {
				incomingLine = sc.nextLine();
				turn += incomingLine;
				
				if(incomingLine.contains( "</turn>" ))
					break;
			}
			
			String[] spact = extractSpeechAct(turn).split( "-" );

			boolean unitive = false;
			boolean differential = false;
			
			String unityPatterns = "acknowledge add agree answer approve complete conclude confirm direct echo elab greet reqConfirm reqDirect reqInfo reqModal reqOpt bye expressPossibility pardon ";
			String differencePatterns = "contrast correct correctSelf negate refuse reject abandon expressImPossibility suggest suggestOpt";
			
			//not patterns: apologise attribute enumerate exclaim explain expressAwareness expressNonAwareness expressConviction expressOpinion expressRegret expressStance expressSurprise expressWish hesitate hold identifySelf init insult offer phatic predict predictPossibility promise refer referCondition referOpt referPerson referReason referTime referThing selfTalk spell state stateIntent stateConstraint stateOpt stateReason summarise swear thirdParty thank unclassifiable uninterpretable
			
			//System.out.println( "id: " + id );

			ArrayList<String> list = new ArrayList<String>();
			for(String s: spact) {
				//System.out.print( s + " "  );
				list.add( s );
				
				if(unityPatterns.contains(s)) unitive = true;
				if(differencePatterns.contains( s )) differential = true;
			}
			//System.out.println();
			
			Utterance u = utters.get( id );
			u.setDifferential( differential );
			u.setUnitive( unitive );
			u.setSpeechActs( list );
			
			utters.set( id, u );
			
		}
		
		sc.close();
		
		l.setUtterances( utters );
		
	}
	
	private static String extractSpeechAct(String s) {
		
		int ind = s.indexOf( "sp-act=\"" );
		if(ind==-1) return "none";
		
		String spactStr = "";
		
		while(ind!=-1) {
			ind+=8;
			int ind2 = s.indexOf( "\"", ind );
			String speechAct = s.substring( ind,  ind2 );
			spactStr += (speechAct + '-');
			
			ind = s.indexOf( "sp-act=\"", ind2 );
			//System.out.println( ind );
		}
		
		spactStr = spactStr.substring( 0, spactStr.length()-1 );
		
		return spactStr;
	}
	
	private static int extractIdNumber(String s) {
		int ind = s.indexOf( "turn n=\"" ) + 8;
		int ind2 = s.indexOf( "\"", ind );
		return Integer.parseInt( s.substring( ind, ind2 ) );
	}
	
	public static String whatPattern(Log l, int id) {
		ArrayList<Utterance> utters = l.getUtterances();
		Utterance u = utters.get( id );
		boolean unitive = u.getUnitive();
		boolean differential = u.getDifferential();
		
		if(unitive && differential) return "both";
		else if(unitive) return "unity";
		else if(differential) return "difference";
		else return "none";
	}
	
	public static int[] howManyUtterances(Log l, Participant p) {
		ArrayList<Utterance> utters = l.getUtterances();
		int unityCount = 0;
		int differenceCount = 0;
		
		for(Utterance u: utters) {
			//System.out.println( u.getSpeaker() + " " + p );
			if(u.getSpeaker()!=p) continue;
			
			if(u.getDifferential()) differenceCount++;
			if(u.getUnitive()) unityCount++;
		}
		
		return new int[] {unityCount, differenceCount};
	}
}
