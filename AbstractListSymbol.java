
abstract class AbstractListSymbol implements ListSymbol {
	
	public BooleanList toList() {
		BooleanList list = new BooleanList();
		list.add(this);
		return list;
	}
}
