package esercizio4;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Guida extends Thread {
	
	private int lingua;
	private Galleria galleria;
	
	public Guida(int lingua, Galleria galleria) {
		this.lingua = lingua;
		this.galleria = galleria;
	}
	
	@Override public String toString() {
		return "Guida " + lingua;
	}
	
	
	@Override public void run() {
		try {
			while(true) {
				System.out.println(this.toString() + " attende la formazione del gruppo..");
				attendi_formazione_gruppo();
				galleria.attendiVisitatori(lingua);
				
				System.out.println(this.toString() + " ha iniziato la visita all'interno "
								+ "della " + galleria.toString());
				visita_galleria();
				
				galleria.terminaVisita(lingua);
				System.out.println(this.toString() + " ha terminato la visita.");
				
				System.out.println(this.toString() + " si prepara per una nuova visita "
						+ "presso la " + galleria.toString());
				riposo();
				
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	private final static int TEMPO_FORMAZIONE_GRUPPO = 20;
	private void attendi_formazione_gruppo() throws InterruptedException {
		TimeUnit.MINUTES.sleep(TEMPO_FORMAZIONE_GRUPPO);
	}
	
	private final static int MIN_TEMPO_VISITA = 2, MAX_TEMPO_VISITA = 3;
	private void visita_galleria() throws InterruptedException {
		Random random = new Random();
		final int TEMPO_VISITA = random.nextInt(MIN_TEMPO_VISITA, MAX_TEMPO_VISITA+1);
		TimeUnit.HOURS.sleep(TEMPO_VISITA);
	}
	
	private final static int TEMPO_RIPOSO = 30;
	private void riposo() throws InterruptedException {
		TimeUnit.MINUTES.sleep(TEMPO_RIPOSO);
	}
	
	
}
