import static org.junit.Assert.*;
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
	public void parserTester(){
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
		assertTrue(b.toString().equals(s.getExpression().toString()));
		
	}
}
