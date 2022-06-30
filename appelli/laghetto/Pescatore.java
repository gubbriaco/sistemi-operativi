package laghetto;

import java.util.Random;

public class Pescatore extends Thread {
	
	private int id;
	private Laghetto l;
	private final static int t = 0;
	
	public Pescatore(int id, Laghetto l) {
		this.id = id;
		this.l = l;
	}
	
	@Override public String toString() {
		return "Pescatore " + id;
	}
	
	@Override public void run() {
		try {
			
			while(true) {
				
				System.out.println("Il " + this.toString() + " intende pescare al lago " + l.toString() + "..");
				l.inizia(t);
				System.out.println("Il " + this.toString() + " sta pescando..");
				stoPescando();
				l.finisci(t);
				System.out.println("Il " + this.toString() + " ha finito di pescare.");
				System.out.println("Il " + this.toString() + " si allontana..");
				miAllontanoERitorno();

			}
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private final static int MIN_TEMPOPESCA = 200, MAX_TEMPO_PESCA = 800, TEMPO_ALLONTANAMENTO = 100;
	private Random random;
	private void stoPescando() throws InterruptedException {
		random = new Random();
		int pesca = random.nextInt(MIN_TEMPOPESCA, (MAX_TEMPO_PESCA+1));
		Thread.sleep(pesca);
	}
	
	private void miAllontanoERitorno() throws InterruptedException {
		Thread.sleep(TEMPO_ALLONTANAMENTO);
	}
	

}
