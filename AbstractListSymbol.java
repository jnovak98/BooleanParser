import java.util.Optional;

abstract class AbstractListSymbol implements ListSymbol {
	
	public BooleanList toList() {
		BooleanList list = new BooleanList();
		list.add(this);
		return list;
	}
	
	@Override
	public Symbol simplified(){
		return this;
	}
	
	@Override
	public Optional<Symbol> subterm() {
		return Optional.empty();
	}
}
