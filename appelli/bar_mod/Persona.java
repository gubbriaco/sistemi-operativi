package bar_mod;

import java.util.Random;

public class Persona extends Thread {
	
	private int id;
	private Bar bar;
	
	private Random random;
	private static final int MAX_TEMPO_PAGARE = 10;
	private static final int MIN_TEMPO_PAGARE = 5;
	private static final int MAX_TEMPO_BERE = 40;
	private static final int MIN_TEMPO_BERE = 20;
	
	private int tempoPagare, tempoBere;
	
	public Persona(int id, Bar bar) {
		this.id = id+1;
		this.bar = bar;
		random = new Random();
		tempoPagare = random.nextInt(MIN_TEMPO_PAGARE, MAX_TEMPO_PAGARE+1);
		tempoBere = random.nextInt(MIN_TEMPO_BERE, MAX_TEMPO_BERE+1);
	}
	
	
	@Override public void run() {
		
		try {
			
			int operazione = bar.scegli();
			
			if( operazione == Bar.CASSA ) {
				
				bar.inizia(Bar.CASSA);
				Thread.sleep(tempoPagare*100);
				bar.finisci(Bar.CASSA);
				
				bar.inizia(Bar.BANCONE);
				Thread.sleep(tempoBere*100);
				bar.finisci(Bar.BANCONE);
				
			}
			else {
				
				bar.inizia(Bar.BANCONE);
				Thread.sleep(tempoBere*100);
				bar.finisci(Bar.BANCONE);
				
				bar.inizia(Bar.CASSA);
				Thread.sleep(tempoPagare*100);
				bar.finisci(Bar.CASSA);
				
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override public String toString() {
		return "cliente " + id; 
	}

}
