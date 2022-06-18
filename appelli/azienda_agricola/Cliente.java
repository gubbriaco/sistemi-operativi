package azienda_agricola;

import java.util.Random;

public class Cliente extends Thread {
	
	private Random random;
	private int numeroSacchetti, numeroSacchettiDaPrelevare, id;
	private static int TEMPO_RITIRO = 1;
	
	private AziendaAgricola aa;
	
	public Cliente(AziendaAgricola aa,int id) {
		random = new Random();
		numeroSacchetti = random.nextInt(1, 11);
		numeroSacchettiDaPrelevare = numeroSacchetti;
		this.aa = aa;
		this.id = id+1;
	}
	
	
	@Override public void run() {
		try {
			
			System.out.println("Il cliente " + id + " ha deciso di acquistare " + numeroSacchetti + " sacchetti.");
			aa.pagamento(numeroSacchetti);
			System.out.println("Il cliente " + id+ "  sta pagando..");
			Thread.sleep(TEMPO_RITIRO*1000);
			System.out.println("Il cliente " + id+ " ha pagato " + numeroSacchetti*aa.COSTO_SACCHETTO + " euro");
			
			System.out.println("Il cliente " + id+ " sta andando in magazzino a ritirare i sacchetti.");
			System.out.println("Il cliente " + id+ " sta ritirando i sacchetti..");
			while(numeroSacchettiDaPrelevare>0) {
				aa.ritira();
				numeroSacchettiDaPrelevare = numeroSacchettiDaPrelevare-1; 
			}
			System.out.println("Il cliente " +id+ " ha terminato di ritirare i sacchetti.");
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
