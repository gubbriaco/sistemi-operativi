package azienda_agricola;

public class Magazziniere extends Thread {
	
	private AziendaAgricola aa;
	
	private static int TEMPO_RIEMPIRE = 10;
	
	
	public Magazziniere(AziendaAgricola aa) {
		this.aa = aa;
	}
	
	@Override public void run() {
		try {
			
			aa.carica();
			System.out.println("Il magazzieniere sta riempendo il magazzino..");
			Thread.sleep(TEMPO_RIEMPIRE*100);
			System.out.println("Il magazziniere ha terminato.");
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
