import java.util.List;
import java.util.ArrayList;

final class WorkingList {
	private final List<Symbol> list;
	
	WorkingList() {
		this.list = new ArrayList<Symbol>();
	}
	
	final boolean add(Symbol symbol){
		return this.list.add(symbol);
	}
	
	final List<Symbol> getList(){
		return list;
	}
	
	final boolean canApplyReduction(Reduction reduction){
		if (this.list.size() >= reduction.size()){
			List<Symbol> sublist = this.list.subList(this.list.size()-reduction.size(), this.list.size());
			List<Type> typeList = new ArrayList<Type>();
			for (Symbol symbol:sublist)
				typeList.add(symbol.getType());
			return reduction.matches(typeList);
		} else
			return false;
	}

	final WorkingList applyReduction(Reduction reduction){
		int splitIndex = this.list.size()-reduction.size();
		//Splits list into unaffected and reducible sections, then applies reduction to reducible section and 
		//adds it back onto unaffected section
		List<Symbol> unreduced = this.list.subList(splitIndex, this.list.size());
		Symbol reducedSymbol = reduction.apply(unreduced);
		unreduced.clear();
		this.list.add(reducedSymbol);
		return this;
	}	
	
}
