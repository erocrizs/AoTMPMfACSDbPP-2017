import java.util.Date;

public class Action implements Comparable<Action> {
	
	protected Date timestamp;
	public Action(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return this.timestamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(Action that) {
		return this.timestamp.compareTo( that.getTimeStamp() );
	}

}
