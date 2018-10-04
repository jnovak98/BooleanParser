import java.util.Objects;
import java.util.Optional;

public final class Expression extends AbstractTreeSymbol {
	private final Symbol leftSubexpression;
	private final Symbol rightSubexpression;
	
	private Expression(Type structure, Symbol leftSubexpression, Symbol rightSubexpression) {
		super(structure);
		this.leftSubexpression = leftSubexpression;
		this.rightSubexpression = rightSubexpression;
	}
	
	//build method for TERM and NOT expressions
	public static final Expression build(boolean isPositive, Symbol subexpression) {
		Objects.requireNonNull(subexpression);
		validateSubexpression(subexpression, Type.TERM, "Argument subexpression must be of Type TERM");	
		Type structureType = isPositive ? Type.TERM : Type.NOT;
		return new Expression(structureType, subexpression, null);
	}
	
	//build method for AND and OR expressions
	public static final Expression build(boolean isConjunction, Symbol leftSubexpression, Symbol rightSubexpression){
		Objects.requireNonNull(leftSubexpression);
		validateSubexpression(leftSubexpression, Type.EXPRESSION, "Argument leftSubexpression must be of Type EXPRESSION");	

		Objects.requireNonNull(rightSubexpression);
		validateSubexpression(rightSubexpression, Type.EXPRESSION, "Argument rightSubexpression must be of Type EXPRESSION");	

		Type structureType = isConjunction ? Type.AND : Type.OR;
		return new Expression(structureType, leftSubexpression, rightSubexpression);
	}

	@Override
	public Type getType() {
		return Type.EXPRESSION;
	}

	@Override
	public BooleanList toList() {
		BooleanList listRepresentation = new BooleanList();
		switch(super.getStructure()){
		case TERM:
			return this.leftSubexpression.toList();
		case NOT:
			listRepresentation.add(Type.NOT);
			listRepresentation.append(this.leftSubexpression.toList());
			return listRepresentation;
		default: //structure is AND or OR
			listRepresentation.append(this.leftSubexpression.toList());
			listRepresentation.add(super.getStructure());
			listRepresentation.append(this.rightSubexpression.toList());
			return listRepresentation;
		}
	}
	
	@Override
	public String toString(){
		return this.toList().toString();
	}

	@Override
	public long complexity() { 
		if((super.getStructure() == Type.AND) || (super.getStructure() == Type.OR)) //combination of 2 terms
			return leftSubexpression.complexity() + rightSubexpression.complexity() + 1;
		else //only one term
			return leftSubexpression.complexity();
	}

	final Symbol getLeftSubexpression() {
		return leftSubexpression;
	}

	final Symbol getRightSubexpression() {
		return rightSubexpression;
	}

	@Override
	public Symbol simplified() {
		if(getStructure() == Type.TERM && leftSubexpression.subterm().get().getType() == Type.EXPRESSION){
			return leftSubexpression.subterm().get().simplified();
		} else {
			boolean isPositiveOrConjunction = (getStructure() == Type.TERM) || (getStructure() == Type.AND);
			if(rightSubexpression == null) //Type is either TERM or NOT
				return Expression.build(isPositiveOrConjunction,  leftSubexpression.simplified());
			else{ //Type is either AND or OR 
				return Expression.build(isPositiveOrConjunction, leftSubexpression.simplified(), rightSubexpression.simplified());
			}
		}
	}

	@Override
	public Optional<Symbol> subterm() {
		System.out.println("Subterm (expression): " + leftSubexpression.subterm().get().toString());
		return (getStructure() == Type.TERM) ? leftSubexpression.subterm() : Optional.empty();
	}
	
}
