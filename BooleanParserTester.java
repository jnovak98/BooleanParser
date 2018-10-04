import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import org.junit.Test;

public class BooleanParserTester {

	@Test
	public void testVariable() {
		//testing build()
		try{
			Variable v = Variable.build(null);
			fail("Should have thrown an error for null String value");
		} catch(NullPointerException e){
		}
		
		//testing toString() and getRepresentation()
		Variable v = Variable.build("A");
		assertTrue(v.toString().equals("A"));
		assertTrue(v.getRepresentation().equals("A"));
	}
	
	@Test
	public void testConnector(){
		//testing build()
		try{
			Connector c = Connector.build(null);
			fail("Should have thrown an error for null Type value");
		} catch(NullPointerException e){
		}
		try{
			Connector c = Connector.build(Type.EXPRESSION);
			fail("Should have thrown an error for incorrect Type value");
		} catch(IllegalArgumentException e){
		}
		
		//testing toString() and getType()
		Connector c = Connector.build(Type.AND);
		assertTrue(c.toString().equals("∧"));
		assertTrue(c.getType().equals(Type.AND));
	}
	
	@Test
	public void testBooleanList(){
		//testing add() and toString()
		BooleanList b = new BooleanList();
		Variable v = Variable.build("A");
		b.add(v);
		assertTrue(b.toString().equals("A"));
		b.add(Type.AND);
		assertTrue(b.toString().equals("A∧"));
		
		//testing freeze()
		BooleanList f = new BooleanList();
		f.freeze();
		try{
			f.add(v);
			fail("Should throw an exception when a list symbol is added to a frozen list");
		} catch(UnsupportedOperationException e){
		}
		
		//testing iterator() and getListRepresentation()
		Iterator<ListSymbol> iter = b.iterator();
		StringBuilder s = new StringBuilder();
		while(iter.hasNext()){
			ListSymbol l = iter.next();
			s.append(l+ " " + l);
		}
		assertTrue(s.toString().equals("A A∧ ∧"));
		
	}
	
	@Test
	public void testTerm(){
		Variable v = Variable.build("A");
		Term t = Term.build(v);
		assertEquals(t.getSymbol(),v);
		assertTrue(t.toString().equals(v.toString()));
		assertTrue(t.toList().toString().equals(v.toString()));
		assertEquals(t.complexity(),0);
		Term t2 = Term.build(Expression.build(true, t));
		assertTrue(t2.toString().equals("(A)"));
		assertTrue(t2.toList().toString().equals("(A)"));
	}
	
	@Test
	public void testExpression(){
		Term t1 = Term.build(Variable.build("A"));
		Term t2 = Term.build(Variable.build("B"));
		Expression e1 = Expression.build(true, t1);
		Expression e2 = Expression.build(false, t2);
		Expression e3 = Expression.build(true, e1, e2);
		assertTrue(e3.toString().equals("A∧¬B"));
		assertTrue(e3.toList().toString().equals("A∧¬B"));
		assertEquals(e3.complexity(),1);
		Expression e4 = Expression.build(false, e1, Expression.build(true, Term.build(e3)));
		assertTrue(e4.toString().equals("A∨(A∧¬B)"));
		assertTrue(e4.toList().toString().equals("A∨(A∧¬B)"));
		assertEquals(e4.complexity(),2);	
	}
	
	@Test
	public void parserAndStateTester(){
		//uses example case in assignment
		BooleanList b = new BooleanList();
		b.add(Variable.build("a"));
		b.add(Type.AND);
		b.add(Type.OPEN);
		b.add(Type.NOT);
		b.add(Variable.build("b"));
		b.add(Type.OR);
		b.add(Variable.build("c"));
		b.add(Type.CLOSE);
		State s = Parser.parse(b);
		//shows that you can convert from tree to list 
		//and back without changing order of terms (since toString for expressions
		//converts to a list)
		assertTrue(b.toString().equals(s.getExpression().toString()));
	}
	
	@Test
	public void workingListAndReductionTester() {
		WorkingList w = new WorkingList();
		Variable a = Variable.build("a");
		w.add(a);
		Reduction r1 = Reduction.build(Arrays.asList(Type.VARIABLE), (list) -> Term.build(list.get(0)));
		Reduction r2 = Reduction.build(Arrays.asList(Type.TERM), (list) -> Expression.build(true,list.get(0)));
		assertTrue(w.canApplyReduction(r1));
		assertFalse(w.canApplyReduction(r2));
		w.applyReduction(r1);
		assertTrue(w.getList().get(0).getType() == Type.TERM);
		assertTrue(w.canApplyReduction(r2));
		assertFalse(w.canApplyReduction(r1));
		w.applyReduction(r2);
		assertTrue(w.getList().get(0).getType() == Type.EXPRESSION);
	}
	
	@Test
	public void simplifyAndSubtermTester() {
		Term t1 = Term.build(Variable.build("A"));
		Expression e1 = Expression.build(true, t1);
		Term t2 = Term.build(e1);
		Expression e2 = Expression.build(true, t2);
		Term t3 = Term.build(e2);
		Expression e3 = Expression.build(true, t3);
		assertTrue(e3.toString().equals("((A))"));
		Symbol e4 = e3.simplified();
		assertTrue(e4.toString().equals("A"));	
		
		Term t4 = Term.build(Variable.build("A"));
		Term t4a = Term.build(Expression.build(true, t4));
		Term t4b = Term.build(Expression.build(true, t4a));
		Term t5 = Term.build(Variable.build("B"));
		Term t5a = Term.build(Expression.build(true, t5));
		Expression e5 = Expression.build(true, t4b);
		Expression e5a = Expression.build(true, Term.build(e5));
		Expression e6 = Expression.build(false, t5a);
		Expression e7 = Expression.build(true, e5a, e6);
		Expression e7a = Expression.build(true, Term.build(e7));
		assertTrue(e7a.toString().equals("((((A)))∧¬(B))"));
		Symbol e8 = e7a.simplified();
		assertTrue(e8.toString().equals("A∧¬(B)"));
	}
}
