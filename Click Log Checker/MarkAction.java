import java.util.Date;

public class MarkAction extends Action {
	
	private Vector2D position;
	private MarkConfig config;
	
	public MarkAction(Date timestamp, Vector2D position, MarkConfig config) {
		super(timestamp);
		this.position = position;
		this.config = config;
	}

	/**
	 * @return the position
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * @return the config
	 */
	public MarkConfig getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(MarkConfig config) {
		this.config = config;
	}
	
	
	
}
