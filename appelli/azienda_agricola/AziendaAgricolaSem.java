package azienda_agricola;

import java.util.concurrent.Semaphore;

public class AziendaAgricolaSem extends AziendaAgricola {
	
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore magazzino;
	private Semaphore cassa = new Semaphore(1, true);
	private Semaphore magazziniere = new Semaphore(0);
	

	public AziendaAgricolaSem(int numeroSacchetti) {
		super(numeroSacchetti);
		magazzino = new Semaphore(numeroSacchetti, true);
	}


	@Override public void pagamento(int numeroSacchetti) throws InterruptedException {
		cassa.acquire();
		incasso = incasso + (numeroSacchetti*COSTO_SACCHETTO);
		cassa.release();
	}

	@Override public void ritira() throws InterruptedException {
		magazzino.acquire();
		
		mutex.acquire();
		sacchettiDisponibili = sacchettiDisponibili-1;
		if(sacchettiDisponibili==0)
			magazziniere.release();
		mutex.release();
	}

	@Override public void carica() throws InterruptedException {
		magazziniere.acquire();
		
		mutex.acquire();
		sacchettiDisponibili = sacchettiDisponibili + buffer.length;
		mutex.release();
		
		magazzino.release(buffer.length);
	}
	
	
	public static void main(String...strings) {
		AziendaAgricola aa = new AziendaAgricolaSem(200);
		aa.test(100);
	}
	
	
}
