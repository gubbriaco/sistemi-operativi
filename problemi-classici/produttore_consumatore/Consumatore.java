package produttore_consumatore;

public class Consumatore extends Thread {
	
	private int id;
	private Buffer b;
	
	public Consumatore(int id, Buffer b) {
		this.id = id;
		this.b = b;
	}
	
	
	@Override public void run() {
		try {
			
			b.get();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}
