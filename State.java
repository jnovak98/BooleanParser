import java.util.List;

public final class State {
	private final boolean correct;
	private final List<Symbol> workingList;
	
	private State(boolean correct, List<Symbol> workingList) {
		super();
		this.correct = correct;
		this.workingList = workingList;
	}
	
	static final State build(List<Symbol> workingList){
		boolean correct = workingList.size() == 1 && workingList.get(0).getType() == Type.EXPRESSION;
		return new State(correct, workingList);
	}

	public final boolean isCorrect() {
		return correct;
	}

	public final List<Symbol> getWorkingList() {
		return workingList;
	}
	
	public final Symbol getExpression(){
		if(correct)
			return workingList.get(0);
		else
			throw new IllegalArgumentException("Cannot perform getExpression() on an incorrect State");
	}
	
	
}
