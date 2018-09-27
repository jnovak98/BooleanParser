import java.util.List;
import java.util.Objects;
import java.util.function.Function;

final class Reduction {
	private final List<Type> pattern;
	private final Function<List<Symbol>, TreeSymbol> reduction;
	
	private Reduction(List<Type> pattern, Function<List<Symbol>, TreeSymbol> reduction) {
		this.pattern = pattern;
		this.reduction = reduction;
	}
	
	static final Reduction build(List<Type> pattern, Function<List<Symbol>, TreeSymbol> reduction){
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(reduction);
		return new Reduction(pattern, reduction);
	}
	
	final int size(){
		return pattern.size();
	}
	
	final boolean matches(List<Type> typeList){
		return (this.size() == typeList.size()) && (this.pattern.equals(typeList));
	}
	
	final Symbol apply(List<Symbol> symbolList){
		return reduction.apply(symbolList);
	}
}
