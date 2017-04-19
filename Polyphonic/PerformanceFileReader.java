import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PerformanceFileReader {
	public static Performance getPerformance(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader( file ) );
		
		ArrayList<String> scoreStrings = new ArrayList<String>();
		String validation = br.readLine();
		if( "slide,timeBug1Found,timeBug2Found,...".equals( validation ) )
		while(true) {
			br.readLine();
			String curr = br.readLine();
			if(curr == null) break;
			
			String[] scores = curr.split(",");
			for(int i=1; i<scores.length; i++) {
				scoreStrings.add( scores[i] );
			}
		}
		
		ArrayList<Long> speedTally = new ArrayList<Long>();
		double accuracy = 0;
		
		for(String score: scoreStrings) {
			if( "-----------------".equals(score) ) continue;
			
			String[] perf = score.split("=");
			speedTally.add( processTimeString( perf[0] ) );
			
			double point = Double.parseDouble( perf[1] );
			accuracy += point;
		}
		
		long speed = -1;
		if( speedTally.size() > 0 ) {
			speed = 0;
			for(long l: speedTally) {
				speed += l;
			}
			speed /= speedTally.size();
		}
		
		br.close();
		return new Performance(speed, accuracy);
	}
	
	private static long processTimeString(String s) {
		long hours = Long.parseLong( s.substring(0, 2) );
		long minutes = Long.parseLong( s.substring(3, 5) );
		long seconds = Long.parseLong( s.substring(6, 8) );
		long milliseconds = Long.parseLong( s.substring(9) );
		
		minutes += hours * 60;
		seconds += minutes * 60;
		milliseconds += seconds * 1000;
		return milliseconds;
	}
	
	public static void main(String[] args) throws IOException {
		Performance p = getPerformance(new File("A.csv"));
		System.out.println(p.accuracy + " " + p.averageSpeed);
		Performance q = getPerformance(new File("B.csv"));
		System.out.println(q.accuracy + " " + q.averageSpeed);
	}
}

class Performance {
	long averageSpeed;
	double accuracy;
	
	public Performance(long averageSpeed, double accuracy) {
		this.averageSpeed = averageSpeed;
		this.accuracy = accuracy;
	}
	
}