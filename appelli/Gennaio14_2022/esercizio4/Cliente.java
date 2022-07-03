package Gennaio14_2022.esercizio4;

import java.util.Random;

public class Cliente extends Thread {
	
	private int id;
	private Cassa cassa;
	
	public Cliente(int id, Cassa cassa) {
		this.id = id;
		this.cassa = cassa;
	}
	
	@Override public String toString() {
		return "Cliente " + id;
	}
	
	
	private Random r;
	private final static int MIN_PRODOTTI = 1, MAX_PRODOTTI = 20;
	
	@Override public void run() {
		try {
			
			r = new Random();
			int N = r.nextInt(MIN_PRODOTTI, MAX_PRODOTTI+1);
			sceglieProdotti(N);
			System.out.println("Il " + this.toString() + " ha scelto gli " + N + " prodotti.");
			
			System.out.println("Il " + this.toString() + " si reca all'unica cassa libera.");
			System.out.println("Il " + this.toString() + " si mette in fila..");
			cassa.svuotaCarrello(N);
			
			System.out.println("E' il turno del " + this.toString());
			cassa.paga(N);
			System.out.println("Il " + this.toString() + " ha pagato " + 5*N + " euro.");
			System.out.println("Il " + this.toString() + " esce dal negozio.");
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void sceglieProdotti(int N) throws InterruptedException {
		int t = 20*N;
		Thread.sleep(t);
	}

}
