package Gennaio14_2022.esercizio4;

import java.util.Random;

public class Cassiere extends Thread {
	
	private Cassa cassa;
	
	public Cassiere(Cassa cassa) {
		this.cassa = cassa;
	}
	
	@Override public String toString() {
		return "cassiere";
	}
	
	
	@Override public void run() {
		try {
			
			while(true) {
				
				cassa.scansiona();
				System.out.println("Il " + this.toString() + " inizia a scansionare i prodotti..");
				stoScansionando();
				
				System.out.println("Il " + this.toString() + " notifica al cliente che deve pagare..");
				
				cassa.prossimoCliente();
				System.out.println("Il " + this.toString() + " attende il prossimo cliente..");
			}
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private Random r;
	
	private void stoScansionando() throws InterruptedException {
		r = new Random();
		int t = r.nextInt(1, 20+1);
		Thread.sleep(t);
	}

}
