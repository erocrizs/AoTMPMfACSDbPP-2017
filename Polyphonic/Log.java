import java.util.*;
import java.time.*;

class Log {
	private ArrayList<Utterance> utterances;
	private Participant[] participants;
	private String fileName;

	public Log(ArrayList<Utterance> list, Participant[] participants, String fileName) {
		utterances = list;
		this.participants = participants;
		this.fileName = fileName;
	}

	public ArrayList<Utterance> getUtterances() {
		return utterances;
	}
	
	public void setUtterances(ArrayList<Utterance> u) {
		utterances = u;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public Participant[] getParticipants() {
		return participants;
	}
	
	public LogData getDate() {
		return null;
	}
}

class Utterance {
	private int id;
	private ArrayList<Word> content;
	private ArrayList<Word> nounLemmaList;
	private ArrayList<Word> properNounList;
	private Participant speaker;
	private boolean codeSwitched;
	private LocalTime time;
	private boolean processSynonyms;
	private ArrayList<Link> implicitLinks;
	private boolean isUnitive = false;
	private boolean isDifferential = false;
	private ArrayList<String> speechActs;

	public Utterance(int id, LocalTime time, ArrayList<Word> content, Participant speaker, boolean codeSwitched) {
		this.id = id;
		this.time = time;
		this.content = content;
		this.speaker = speaker;
		this.codeSwitched = codeSwitched;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Word> getContent() {
		return content;
	}

	public String getContentString() {
		String s = "";
		for(Word w: getContent()) {
			s += w.getContent() + " ";
		}
		s.trim();
		return s;
	}

	public Participant getSpeaker() {
		return speaker;
	}

	public boolean isCodeSwitched() {
		return codeSwitched;
	}
	public boolean processedSynonyms() {
		return processSynonyms;
	}

	public void setProcessedSynonyms(boolean b) {
		processSynonyms = b;
	}

	public LocalTime getTime() {
		return time;
	}
	
	public ArrayList<Word> getNounLemmaList() {
		return nounLemmaList;
	}
	
	public void setNounLemmaList(ArrayList<Word> nounLemmaList) {
		this.nounLemmaList = nounLemmaList;
	}
	
	public ArrayList<Word> getProperNounList() {
		return properNounList;
	}
	
	public void setProperNounList(ArrayList<Word> properNounList) {
		this.properNounList = properNounList;
	}
	
	public ArrayList<Word> getTopics() {
		ArrayList<Word> result = new ArrayList<Word>();
		result.addAll( this.getNounLemmaList() );
		result.addAll( this.getProperNounList() );
		result.sort( (a, b) -> {
			String aWord = a.getContent();
			String bWord = b.getContent();
			return aWord.compareTo( bWord );
		} );
		
		if(result.size() > 0) {
			String lastWord = result.get( result.size()-1 ).getContent();
			for(int i=result.size()-2; i>=0; i--) {
				String currWord = result.get( i ).getContent();
				if( lastWord.equals( currWord ) ) {
					result.remove(i);
				} else {
					lastWord = currWord;
				}
			}
		}
		return result;
	}

	public ArrayList<Link> getImplicitLinks() {
		return implicitLinks;
	}

	public void setImplicitLinks(ArrayList<Link> list) {
		implicitLinks = list;
	}
	
	public void setSpeechActs(ArrayList<String> arr) {
		speechActs = arr;
	}
	
	public ArrayList<String> getSpeechActs() {
		return speechActs;
	}
	
	public void setUnitive(boolean bool) {
		isUnitive = bool;
	}
	
	public boolean getUnitive() {
		return isUnitive;
	}
	
	public void setDifferential (boolean bool) {
		isDifferential = bool;
	}
	
	public boolean getDifferential() {
		return isDifferential;
	}
	
	public UtteranceData getData(ContributionCounter cc) {
		int isCodeSwitched = this.codeSwitched ? UtteranceData.CODE_SWITCHED : UtteranceData.NOT_CODE_SWITCHED;
		int utteranceStrength = cc.getUtteranceStrength(this);
		int pattern = -1;
		
		if( isUnitive && isDifferential ) {
			pattern = UtteranceData.BOTH_PATTERN;
		} else if( isUnitive ) {
			pattern = UtteranceData.UNITIVE_PATTERN;
		} else if( isDifferential ) {
			pattern = UtteranceData.DIFFERENCE_PATTERN;
		} else {
			pattern = UtteranceData.NO_PATTERN;
		}
		
		return new UtteranceData( isCodeSwitched, utteranceStrength,pattern	);
	}
}

class Participant {
	private String codename;
	private String realName;
	private ArrayList<Utterance> utterances;

	public Participant(String codename, String realName) {
		this.codename = codename;
		this.realName = realName;
		this.utterances = new ArrayList<Utterance>();
	}

	public String getCodeName() {
		return codename;
	}

	public String getRealName() {
		return realName;
	}
	
	public void addUtterance( Utterance utter ) {
		this.utterances.add( utter );
	}
	
	public List<Utterance> getUtterances() {
		return this.utterances;
	}
	
	public ParticipantData getData(String parentPath) {
		String fileName = parentPath + "/" + codename.charAt( codename.length() - 1 ) + ".csv";
		return null;
	}
}

class Word {
	private String content;
	private List<String> synonyms = null;
	private int index;

	public Word(String content) {
		this.content = content;
		this.index = -1;
	}
	
	public Word(String content, int index) {
		this(content);
		this.index = index;
	}

	public String getContent() {
		return content;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}
	
	public int getIndex() {
		return index;
	}
}

class Link {
	private int utteranceIdA;
	private int wordIndexA;
	private int utteranceIdB;
	private int wordIndexB;

	public Link(int utteranceIdA, int wordIndexA, int utteranceIdB, int wordIndexB) {
		this.utteranceIdA = utteranceIdA;
		this.wordIndexA = wordIndexA;
		this.utteranceIdB = utteranceIdB;
		this.wordIndexB = wordIndexB;
	}

	public int getUtteranceIdA() {
		return utteranceIdA;
	}
	public int getWordIndexA() {
		return wordIndexA;
	}
	public int getUtteranceIdB() {
		return utteranceIdB;
	}
	public int getWordIndexB() {
		return wordIndexB;
	}
}