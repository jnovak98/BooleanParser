import java.util.HashMap;
import java.util.Map;

public class Connector extends AbstractListSymbol implements ListSymbol {
	private final Type type;
	
	private static Map<Type, String> connectorTypesAndSymbols = new HashMap<Type, String>() {{
		put(Type.OR,"\u2228");
		put(Type.AND,"\u2227");
		put(Type.NOT,"\u00AC");
		put(Type.OPEN,"(");
		put(Type.CLOSE,")");
	}};
	
	private Connector(Type type) {
		this.type = type;
	}
	
	//Only creates a new Connector object if it has a non-null and allowed value
	public static final Connector build(Type type){
		if(type == null)
			throw new NullPointerException("Cannot build a Connector object with null String value");
		if (! connectorTypesAndSymbols.containsKey(type))
			throw new IllegalArgumentException("Connector must have Type value of OR, AND, NOT, OPEN, or CLOSE");
		
		return new Connector(type);
	}
	
	//returns the Unicode representation of the object's type
	@Override
	public String toString(){
		return connectorTypesAndSymbols.get(type);
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public long complexity() {
		return (type == Type.OR || type == Type.AND) ? 1 : 0;
	}

}
