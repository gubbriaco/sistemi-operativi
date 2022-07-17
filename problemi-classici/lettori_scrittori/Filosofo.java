package lettori_scrittori;

import java.util.Random;

public class Filosofo extends Thread {
	
	private int id;
	private Tavola t;
	
	public Filosofo(int id, Tavola t) {
		this.id = id;
		this.t = t;
	}
	
	
	@Override public void run() {
		try {
			
			while(true) {
			
				int pos = new Random().nextInt(0, t.getNumeroPosti());
				t.mangia(pos);
				t.pensa(pos);
			
			}
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
