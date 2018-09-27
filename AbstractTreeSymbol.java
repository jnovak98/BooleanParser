import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

public abstract class AbstractTreeSymbol implements TreeSymbol{
	private final Type structure;
		
	protected AbstractTreeSymbol(Type structure) {
		this.structure = structure;
	}
	
	//Throws error if subexpression DNE or is not included in expectedTypes
	//subexpressionDescription is included in error message
	protected static final void validateSubexpression(Symbol subexpression, 
			Set<Type> expectedTypes, String subexpressionDescription) {
		Objects.requireNonNull(subexpression);
		if(! expectedTypes.contains(subexpression.getType())){
			throw new IllegalArgumentException(subexpressionDescription);
		}
	}
	
	//same method with slightly adjusted parameters
	protected static final void validateSubexpression(Symbol subexpression, 
			Type expectedType, String subexpressionDescription) {
		Set<Type> expectedTypes = new HashSet<>(); 
		expectedTypes.add(expectedType);
		validateSubexpression(subexpression, expectedTypes, subexpressionDescription);
	}

	public Type getStructure() {
		return structure;
	}
}
