import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public final class BooleanList implements Iterable<ListSymbol> {
	private final List<ListSymbol> listRepresentation = new ArrayList<ListSymbol>();
	private boolean frozen = false;
	
	//adds a ListSymbol to the boolean list if the list hasn't been frozen
	public final boolean add(ListSymbol listSymbol) {
		if(! frozen){
			return listRepresentation.add(listSymbol);
		} else {
			throw new UnsupportedOperationException("Cannot modify BooleanList after it has been frozen");
		}
	}
	
	//builds a new Connector of Type type and adds it to the boolean list
	public final boolean add(Type type){
		Connector con = Connector.build(type);
		return this.add(con);
	}
	
	//adds the contents of the parameter BooleanList and returns whether
	//these contents were successfully added 
	protected final boolean append(BooleanList list){
		boolean appendSuccess = true;
		for(ListSymbol symbol:list)
			//if any addition fails, appendSuccess will turn false
			appendSuccess = appendSuccess && (this.add(symbol));
		return appendSuccess;
		
	}
	
	public final void freeze() {
		this.frozen = true;
	}

	public final List<ListSymbol> getListRepresentation() {
		return new ArrayList<ListSymbol> (listRepresentation);
	}
	
	@Override
	public String toString(){
		StringBuilder s = new StringBuilder();
		for(ListSymbol symbol: listRepresentation){
			s.append(symbol.toString());
		}
		return s.toString();
	}
	
	public Iterator<ListSymbol> iterator(){
		return getListRepresentation().iterator();
	}
	
	public final long complexity() {
		long complexityCount = 0;
		for(ListSymbol symbol: listRepresentation){
			complexityCount += symbol.complexity();
		}
		return complexityCount;
	}
}
