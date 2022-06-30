package laghetto;

import java.util.Random;

public class Addetto extends Thread {
	
	private int id;
	private Laghetto l;
	private final static int t = 1;
	
	public Addetto(int id, Laghetto l) {
		this.id = id;
		this.l = l;
	}
	
	@Override public String toString() {
		return "Addetto " + id;
	}
	
	@Override public void run() {
		try {
			
			while(true) {
				
				l.inizia(t);
				System.out.println("L' " + this.toString() + " sta ripopolando il " + l.toString() + "..");
				stoRipopolando();
				l.finisci(t);
				System.out.println("L' " + this.toString() + " ha terminato di ripopolare il " + l.toString() + ".");
				miAllontano();
				
			}
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private final static int MIN_TEMPO_RIPOPOLAMENTO = 300, MAX_TEMPO_RIPOPOLAMENTO = 600, TEMPO_ALLONTAMENTO = 300; 
	private Random random;
	private void stoRipopolando() throws InterruptedException {
		random = new Random();
		int ripopolare = random.nextInt(MIN_TEMPO_RIPOPOLAMENTO, (MAX_TEMPO_RIPOPOLAMENTO+1));
		Thread.sleep(ripopolare);
	}
	
	private void miAllontano() throws InterruptedException {
		Thread.sleep(TEMPO_ALLONTAMENTO);
	}

}
