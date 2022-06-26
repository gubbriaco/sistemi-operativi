package trenino;

import java.util.Random;

public class Turista extends Thread {
	
	private int ID;
	private Trenino trenino;
	
	@SuppressWarnings("unused")
	private Random random;
	@SuppressWarnings("unused")
	private static int MIN_TEMPO_IN_CABINA = 2, MAX_TEMPO_IN_CABINA = 5;
	
	public Turista(int ID, Trenino trenino) {
		this.ID = ID;
		this.trenino = trenino;
		random = new Random();
	}
	
	
	@Override public void run() {
		
		try {
			
			trenino.turistaSali();
//			int tempoInCabina = random.nextInt( MIN_TEMPO_IN_CABINA*100, (MAX_TEMPO_IN_CABINA+1)*100 );
//			Thread.sleep(tempoInCabina);
			trenino.turistaScendi();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override public String toString() {
		return "Turista " + ID;
	}

}
