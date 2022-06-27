package ferryboat;

import java.util.Random;

public class Auto extends Thread {
	
	private int id;
	private FerryBoat ferryboat;
	private Random random;
	
	private static int MIN_TEMPO_SPEGNIMENTO_MOTORE = 1, MAX_TEMPO_SPEGNIMENTO_MOTORE = 2;
	
	public Auto(int id, FerryBoat ferryboat) {
		this.id = id;
		this.ferryboat = ferryboat;
	}
	
	@Override public void run() {
		
		try {
			random = new Random();
			
			ferryboat.sali();
			int tempoSpegnimentoMotore = random.nextInt( MIN_TEMPO_SPEGNIMENTO_MOTORE*100,
													     MAX_TEMPO_SPEGNIMENTO_MOTORE*100);
			Thread.sleep( tempoSpegnimentoMotore );
			ferryboat.parcheggiaEScendi();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override public String toString() {
		return "Auto " + id; 
	}

}
