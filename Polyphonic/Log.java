import java.util.*;
import java.time.*;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;

class Log {
	private ArrayList<Utterance> utterances;

	public Log(ArrayList<Utterance> list) {
		utterances = list;
	}

	public ArrayList<Utterance> getUtterances() {
		return utterances;
	}
}

class Utterance {
	private int id;
	private ArrayList<Word> content;
	private Participant speaker;
	private boolean codeSwitched;
	private LocalTime time;
	private boolean processSynonyms;
	private ArrayList<Link> implicitLinks;

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

	public ArrayList<Link> getImplicitLinks() {
		return implicitLinks;
	}

	public void setImplicitLinks(ArrayList<Link> list) {
		implicitLinks = list;
	}
}

class Participant {
	private String codename;
	private String realName;

	public Participant(String codename, String realName) {
		this.codename = codename;
		this.realName = realName;
	}

	public String getCodeName() {
		return codename;
	}

	public String getRealName() {
		return realName;
	}
}

class Word {
	private String content;
	private List<String> synonyms = null;

	public Word(String content) {
		this.content = content;
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
		return utteranceIdA;
	}
	public int getWordIndexB() {
		return wordIndexB;
	}
}