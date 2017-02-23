
public abstract class Adjuster {
	
	protected Adjuster next;
	public Adjuster( Adjuster next ) {
		this.next = next;
	}
	
	protected final void adjust( String[][] input ) {
		this.applyAdjustment( input );
		if( this.next != null )
			this.next.adjust( input );
	}

	public abstract void applyAdjustment(String[][] input);
	
}
