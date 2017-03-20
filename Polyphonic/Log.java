import java.util.*;
import java.time.*;

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

	public LocalTime getTime() {
		return time;
	}
}

class Participant {
	private String name;

	public Participant(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class Word {
	private String content;

	public Word(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}