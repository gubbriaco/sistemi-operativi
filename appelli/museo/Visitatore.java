package museo;

import java.util.Random;

public class Visitatore extends Thread {
	
	private int codice;
	private Museo museo;
	
	private Random random;
	private static int MIN_TEMPO_ARCHEOLOGICA = 20, MAX_TEMPO_ARCHEOLOGICA = 40;
	private int tempoInArcheologica;
	private static int MIN_TEMPO_DAMA = 5, MAX_TEMPO_DAMA = 8;
	private int tempoInDama;
	
	public Visitatore(int codice, Museo museo) {
		this.codice = codice;
		this.museo = museo;
		
		random = new Random();
	}
	
	
	@Override public void run() {
		try {
			
			museo.visitaSA();
			tempoInArcheologica = random.nextInt( MIN_TEMPO_ARCHEOLOGICA*10, (MAX_TEMPO_ARCHEOLOGICA+1)*10 );
			Thread.sleep(tempoInArcheologica);
			museo.terminaVisitaSA();
			
			museo.visitaSD();
			tempoInDama = random.nextInt( MIN_TEMPO_DAMA*10, (MAX_TEMPO_DAMA+1)*10 );
			Thread.sleep(tempoInDama);
			museo.terminaVisitaSD();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	@Override public String toString() {
		return "Visitatore " + codice;
	}
	
}
