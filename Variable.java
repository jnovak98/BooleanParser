
public final class Variable extends AbstractListSymbol implements ListSymbol, TreeSymbol {
	private final String representation;
	
	private Variable(String representation) {
		this.representation = representation;
	}
	
	//Throws an error if the String inputted is null
	public static final Variable build(String representation){
		if(representation != null)
			return new Variable(representation);
		else
			throw new NullPointerException("Cannot build a Variable object with null String value");
	}
	
	public final String getRepresentation() {
		return representation;
	}
	
	@Override
	public String toString(){
		return this.getRepresentation();
	}

	@Override
	public Type getType() {
		return Type.VARIABLE;
	}
	
	@Override 
	public long complexity() {
		return 0;
	}
}
