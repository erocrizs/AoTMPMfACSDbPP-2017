
class LogData {
	public int utterance;
	public double averageAccuracy;
	
	public int unitiveUtteranceCount;
	public int differenceUtteranceCount;
	public int codeSwitchedCount;
	
	public double unitiveUtterancePercentage;
	public double differenceUtterancePercentage;
	public double codeSwitchedPercentage;
	
	public String name;
	public int color;

	public LogData(int utterance, double averageAccuracy, int unitiveUtteranceCount, int differenceUtteranceCount,
			int codeSwitchedCount, String name, int color) {
		this.utterance = utterance;
		this.averageAccuracy = averageAccuracy;
		
		this.unitiveUtteranceCount = unitiveUtteranceCount;
		this.differenceUtteranceCount = differenceUtteranceCount;
		this.codeSwitchedCount = codeSwitchedCount;
		
		if( this.utterance > 0 ) {
			this.unitiveUtterancePercentage = ( 100.0 * this.unitiveUtteranceCount ) / this.utterance;
			this.differenceUtterancePercentage = ( 100.0 * this.differenceUtteranceCount ) / this.utterance;
			this.codeSwitchedPercentage = ( 100.0 * this.codeSwitchedCount ) / this.utterance;
		}
		
		this.name = name;
		this.color = color;
	}
}

class ParticipantData {
	public long speed;
	public double accuracy;
	public int contribution;
	public int utterance;
	
	public int unitiveUtteranceCount;
	public int differenceUtteranceCount;
	public int codeSwitchedCount;
	
	public double unitiveUtterancePercentage;
	public double differenceUtterancePercentage;
	public double codeSwitchedPercentage;
	
	public String name;
	public int color;
	public int glyph;

	public ParticipantData(long speed, double accuracy, int contribution, int utterance, int unitiveUtteranceCount,
			int differenceUtteranceCount, int codeSwitchedCount, String name, int color) {
		this.speed = speed;
		this.accuracy = accuracy;
		this.contribution = contribution;
		this.utterance = utterance;
		
		this.unitiveUtteranceCount = unitiveUtteranceCount;
		this.differenceUtteranceCount = differenceUtteranceCount;
		this.codeSwitchedCount = codeSwitchedCount;
		
		if( this.utterance > 0 ) {
			this.unitiveUtterancePercentage = ( 100.0 * this.unitiveUtteranceCount ) / this.utterance;
			this.differenceUtterancePercentage = ( 100.0 * this.differenceUtteranceCount ) / this.utterance;
			this.codeSwitchedPercentage = ( 100.0 * this.codeSwitchedCount ) / this.utterance;
		}
		
		this.name = name;
		this.color = color;
		
		if( name.substring( name.length()-1 ).equals("A") ) {
			this.glyph = 2;
		} else {
			this.glyph = 1;
		}
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
	public String name;
	
	public UtteranceData(int isCodeSwitched, int strength, int pattern, String name) {
		this.isCodeSwitched = isCodeSwitched;
		this.strength = strength;
		this.pattern = pattern;
		this.name = name;
	}
}