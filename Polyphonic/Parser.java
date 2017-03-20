import java.util.*;
import java.time.*;
import java.time.format.*;
import java.io.*;

public class Parser {
	public static void main(String[] args) {
		Log l = createLog("ADDU-SP02A-SP02B.in");
		printLog(l);
	}

	public static Log createLog(String fileName) {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(fileName));
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
			return null;
		}

		ArrayList<Utterance> log = new ArrayList<Utterance>();
		int id = 0;

		Participant[] p = {new Participant(sc.nextLine()), new Participant(sc.nextLine())};
		
		while(sc.hasNextLine()) {
			String s1 = sc.nextLine();
			if(s1.equals("")) continue;
			String s2 = sc.nextLine();
			String s3 = sc.nextLine();

			//make utterances
			Participant speaker = p[parseParticipant(s1)];
			LocalTime time = parseTime(s1);
			boolean codeSwitched = s2.equals("N"); //N means it was in English, which may be the second language of the participants, therefore we considered an English utterance to be code-switched
			ArrayList<Word> content = parseWords(s3);

			Utterance u = new Utterance(id, time, content, speaker, codeSwitched);
			log.add(u);
		}

		return new Log(log);
	}

	private static int parseParticipant(String s) {
		int ind = s.indexOf(':');
		char c = s.charAt(ind-1);
		return c-'A';
	}

	private static LocalTime parseTime(String s) {
		String t = s.substring( s.indexOf(':') + 1 );
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:m:s a");
		LocalTime time = LocalTime.parse(t, formatter);
		return time;
	}

	private static ArrayList<Word> parseWords(String s) {
		String[] arr = s.split(" ");
		ArrayList<Word> list = new ArrayList<Word>();
		for(String x: arr) {
			list.add(new Word(x));
		}

		return list;
	}

	public static void printLog(Log l) {
		for(Utterance u: l.getUtterances()) {
			System.out.printf("id: %d \t speaker: %s\n", u.getId(), u.getSpeaker().getName());
			System.out.println("code-switched? " + u.isCodeSwitched() + "\t time: " + u.getTime().toString());
			System.out.println(u.getContentString());

			System.out.println();
		}

	}
}