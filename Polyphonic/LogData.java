
class LogData {
	public int utterance;
	public double averageAccuracy;
	public int unitiveUtteranceCount;
	public int differenceUtteranceCount;
	public int codeSwitchedCount;
	
	public LogData(int utterance, double averageAccuracy, int unitiveUtteranceCount, int differenceUtteranceCount,
			int codeSwitchedCount) {
		this.utterance = utterance;
		this.averageAccuracy = averageAccuracy;
		this.unitiveUtteranceCount = unitiveUtteranceCount;
		this.differenceUtteranceCount = differenceUtteranceCount;
		this.codeSwitchedCount = codeSwitchedCount;
	}
}

class ParticipantData {
	public long speed;
	public double accuracy;
	public int unitiveUtteranceCount;
	public int differenceUtteranceCount;
	public int contribution;
	
	public ParticipantData(long speed, double accuracy, int unitiveUtteranceCount, int differenceUtteranceCount,
			int contribution) {
		this.speed = speed;
		this.accuracy = accuracy;
		this.unitiveUtteranceCount = unitiveUtteranceCount;
		this.differenceUtteranceCount = differenceUtteranceCount;
		this.contribution = contribution;
	}
}

class UtteranceData {
	public static final int CODE_SWITCHED = 1;
	public static final int NOT_CODE_SWITCHED = 2;
	
	public static final int UNITIVE_PATTERN = 1;
	public static final int DIFFERENCE_PATTERN = 2;
	public static final int NO_PATTERN = 3;
	public static final int BOTH_PATTERN = 4;
	
	public int isCodeSwitched;
	public int strength;
	public int pattern;
	
	public UtteranceData(int isCodeSwitched, int strength, int pattern) {
		this.isCodeSwitched = isCodeSwitched;
		this.strength = strength;
		this.pattern = pattern;
	}
}