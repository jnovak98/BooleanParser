import java.util.HashSet;
import java.util.Set;

public class Term extends AbstractTreeSymbol {
	private final Symbol subexpression;
	
	private Term(Symbol subexpression){
		super(subexpression.getType());
		this.subexpression = subexpression;
	}

	public static final Term build(Symbol subexpression) {
		Set<Type> allowedTypes = new HashSet<>(); 
		allowedTypes.add(Type.VARIABLE);
		allowedTypes.add(Type.EXPRESSION);
		validateSubexpression(subexpression, allowedTypes, "Terms must consist of either a single VARIABLE or an EXPRESSION");
		return new Term(subexpression);
	}
	
	@Override
	public Type getType() {
		return Type.TERM;
	}

	final Symbol getSymbol() {
		return subexpression;
	}

	@Override
	public BooleanList toList() {
		if(subexpression.getType() == Type.VARIABLE){
			return subexpression.toList();
		} else { //we know it is an EXPRESSION because build() only allows Variables and Expressions
			BooleanList listRepresentation = new BooleanList();
			listRepresentation.add(Type.OPEN);
			//adds everything in BooleanList of subexpression
			listRepresentation.append(subexpression.toList());
			listRepresentation.add(Type.CLOSE);
			return listRepresentation;
		}
	}
	
	@Override
	public String toString() {
		return this.toList().toString();
	}

	@Override
	public long complexity() {
		return subexpression.complexity();
	}

}
