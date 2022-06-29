package centro_storico;

import java.util.Random;

public class Guida extends Thread {
	
	private TourFirenze tf;
	
	private static int TEMPO_DESCRIZIONE_ATTIVITA = 15*10, TEMPO_PRIMA_PARTE, TEMPO_PAUSA, TEMPO_SECONDA_PARTE;
	private static int MIN_TEMPO_PRIMA_PARTE = 40*10, MAX_TEMPO_PRIMA_PARTE = 50*10;
	private static int MIN_TEMPO_PAUSA = 10*10, MAX_TEMPO_PAUSA = 20*10;
	private static int MIN_TEMPO_SECONDA_PARTE = 40*10, MAX_TEMPO_SECONDA_PARTE = 50*10;
	private Random random;
	
	public Guida(TourFirenze tf) {
		this.tf = tf;
		random = new Random();
	}
	
	
	@Override public void run() {
		try {
			while(tf.nr_turisti > 0) {
				tf.attendiFormazioneGruppo();
				Thread.sleep( TEMPO_DESCRIZIONE_ATTIVITA );
				tf.visitaInizia();
				TEMPO_PRIMA_PARTE  = random.nextInt( MIN_TEMPO_PRIMA_PARTE, MAX_TEMPO_PRIMA_PARTE+1 ); 
				Thread.sleep( TEMPO_PRIMA_PARTE );
				TEMPO_PAUSA = random.nextInt( MIN_TEMPO_PAUSA, MAX_TEMPO_PAUSA+1 );
				Thread.sleep( TEMPO_PAUSA );
				TEMPO_SECONDA_PARTE = random.nextInt( MIN_TEMPO_SECONDA_PARTE, MAX_TEMPO_SECONDA_PARTE+1 );
				Thread.sleep( TEMPO_SECONDA_PARTE );
				tf.visitaFine();
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override public String toString() {
		return "Guida ";
	}
	

}
