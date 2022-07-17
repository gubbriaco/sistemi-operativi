package produttore_consumatore;

import java.util.Random;

public class Produttore extends Thread {
	
	private int id;
	private Buffer b;
	
	public Produttore(int id, Buffer b) {
		this.id = id;
		this.b = b;
	}
	
	
	@Override public void run() {
		try {
			
			b.put(new Random().nextInt(0, b.getCapienza()));
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
