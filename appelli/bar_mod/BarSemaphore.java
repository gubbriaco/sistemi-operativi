package bar_mod;

import java.util.concurrent.Semaphore;

public class BarSemaphore extends Bar {

	/** per modificare le variabili interne in mutua-esclusione */
	private Semaphore mutex = new Semaphore(1);

	private Semaphore[] fila = new Semaphore[NUMERO_FILE];
	
	protected int[] numeroPersoneInFila = new int[NUMERO_FILE];
	
	
	public BarSemaphore() {
		super();
		for(int i=0;i<numeroPersoneInFila.length;++i) {
			fila[i] = new Semaphore(MAX_PERSONE_PER_FILA[i], true);
			numeroPersoneInFila[i] = 0;
		}
	}


	@Override public int scegli() throws InterruptedException {
		int operazione = -1;
		mutex.acquire();
		
		if( numeroPostiLiberi[CASSA] == 0 )
			operazione = CASSA;
		else if( numeroPostiLiberi[BANCONE] == 0 )
			operazione = CASSA;
		else if( numeroPersoneInFila[CASSA] <= numeroPersoneInFila[BANCONE] )
			operazione = CASSA;
		else
			operazione = BANCONE;
		
		System.out.println( "Il " + Thread.currentThread().toString() + " vuole andare " +
						  "verso " + (operazione==CASSA?" la cassa":" il bancone") );
		
		mutex.release();
		return operazione;
	}


	@Override public void inizia(int i) throws InterruptedException {
		mutex.acquire();
		numeroPersoneInFila[i]++;
		mutex.release();
		
		fila[i].acquire(); /** acquisisco un solo permesso relativo alla i-sima fila */
		
		mutex.acquire();
		
		numeroPersoneInFila[i]--;
		numeroPostiLiberi[i]--; /** decremento il numero di posti liberi della i-sima fila */
		
		System.out.println("Il " + Thread.currentThread().toString() + " si trova " +
						   (i==CASSA?"in cassa":"al bancone"));
		
		mutex.release();
	}


	@Override public void finisci(int i) throws InterruptedException {
		mutex.acquire();
		
		numeroPersoneInFila[i]--;
		numeroPostiLiberi[i]++;
		fila[i].release();
		
		System.out.println("Il " + Thread.currentThread().toString() + " ha terminato " +
						   (i==CASSA?"alla cassa":"al bancone"));
		System.out.println("Il " + Thread.currentThread().toString() + " esce dal bar");
		
		mutex.release();
	}
	
	
	
	public static void main(String...strings) {
		
		Bar bar = new BarSemaphore();
		bar.test(100);
		
	}
	

}
