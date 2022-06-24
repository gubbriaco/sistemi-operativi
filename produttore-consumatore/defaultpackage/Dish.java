package defaultpackage;

public class Dish {
	
	private int ID;
	
	public Dish(int ID) {
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
	
	@Override public String toString() {
		return "Dish " + ID;
	}

}
