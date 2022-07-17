package lettori_scrittori.semaphore;

import java.util.concurrent.Semaphore;

import lettori_scrittori.Tavola;

public class TavolaSem extends Tavola {

	private Semaphore mutex = new Semaphore(1);
	private Semaphore[] tavola;
	private Semaphore modifica_posti = new Semaphore(1);
	
	public TavolaSem(int numero_posti) {
		super(numero_posti);
		
		tavola = new Semaphore[numero_posti];
		for(int i=0;i<numero_posti;++i)
			tavola[i] = new Semaphore(1);
	}

	@Override public void mangia(int i) throws InterruptedException {
		mutex.acquire();
		modifica_posti.acquire();
		
		int i1 = i;
		int i2 = (i1+1)%this.getNumeroPosti();
		tavola[i1].acquire();
		tavola[i2].acquire();
		
		modifica_posti.release();
		mutex.release();
	}

	@Override public void pensa(int i) throws InterruptedException {
		mutex.acquire();
		modifica_posti.acquire();
		
		int i1 = i;
		int i2 = (i1+1)%this.getNumeroPosti();
		tavola[i1].release();
		tavola[i2].release();
		
		modifica_posti.release();
		mutex.release();
	}
	
	
	public static void main(String...strings) {
		new TavolaSem(10).test(5);
	}
	

}
