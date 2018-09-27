import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public final class Parser {
	private static final List<Reduction> reductionsList = new ArrayList<Reduction>() {{
				//VARIABLE to TERM
				add(Reduction.build(Arrays.asList(Type.VARIABLE), (list) -> Term.build(list.get(0))));
				//OPEN,EXPRESSION,CLOSE to TERM
				add(Reduction.build(Arrays.asList(Type.OPEN,Type.EXPRESSION,Type.CLOSE), (list) -> Term.build(list.get(1))));
				//NOT,TERM to EXPRESSION
				add(Reduction.build(Arrays.asList(Type.NOT,Type.TERM), (list) -> Expression.build(false,list.get(1))));
				//TERM to EXPRESSION
				add(Reduction.build(Arrays.asList(Type.TERM), (list) -> Expression.build(true,list.get(0))));
				//EXPRESSION,AND,EXPRESSION to EXPRESSION
				add(Reduction.build(Arrays.asList(Type.EXPRESSION,Type.AND,Type.EXPRESSION),
						(list) -> Expression.build(true,list.get(0),list.get(2))));
				//EXPRESSION,OR,EXPRESSION to EXPRESSION
				add(Reduction.build(Arrays.asList(Type.EXPRESSION,Type.OR,Type.EXPRESSION),
						(list) -> Expression.build(false,list.get(0),list.get(2))));
			}};
	

	public static final State parse(BooleanList input){
		WorkingList workingList = new WorkingList();
		for(ListSymbol symbol:input){
			workingList.add(symbol); 
			reduce(workingList);
		}
		return State.build(workingList.getList());
	}
	
	private static final WorkingList reduce(WorkingList workingList){
		for(Reduction reduction: reductionsList){
			if(workingList.canApplyReduction(reduction)){
				//recurses on reduced WorkingList
				return reduce(workingList.applyReduction(reduction));
			} else{
				//the reduction doesn't match, so do nothing 
			}
		}
		//if this line is reached, then no more reductions can be applied, so we return the reduced list
		return workingList; 
	}
}
