import java.util.Optional;

public interface Symbol {
	public Type getType();
	public BooleanList toList();
	public long complexity();
	public Symbol simplified();
	public Optional<Symbol> subterm(); 
}
