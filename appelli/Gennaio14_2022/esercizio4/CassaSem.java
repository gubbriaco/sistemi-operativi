package Gennaio14_2022.esercizio4;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class CassaSem extends Cassa {
	
	private Semaphore mutex = new Semaphore(1, true);
	private Semaphore prossimo = new Semaphore(1, true);
	private Semaphore puoiScansionare = new Semaphore(0, true);
	private Semaphore puoiPagare= new Semaphore(0, true);
	private Semaphore esco = new Semaphore(0);
	
	private LinkedList<Thread> inFila = new LinkedList<>(), hannoPagato = new LinkedList<>();
 
	@Override public void svuotaCarrello(int N) throws InterruptedException {
		Thread cliente = Thread.currentThread();
		mutex.acquire();
		inFila.addLast(cliente);
		System.out.println("IN FILA: " + inFila.toString());
		mutex.release();
		
		prossimo.acquire();
		
		mutex.acquire();
		inFila.remove(cliente);
		System.out.println("IN FILA: " + inFila.toString());
		mutex.release();
		
		puoiScansionare.release();
	}

	@Override public void scansiona() throws InterruptedException {
		puoiScansionare.acquire();
		
		puoiPagare.release();
		
	}

	@Override public void paga(int N) throws InterruptedException {
		puoiPagare.acquire();
		
		Thread cliente = Thread.currentThread();
		mutex.acquire();
		hannoPagato.addLast(cliente);
		System.out.println("HANNO PAGATO: " + hannoPagato.toString());
		mutex.release();
		
		esco.release();
	}

	@Override public void prossimoCliente() throws InterruptedException {
		esco.acquire();
		
		prossimo.release();
	}
	
	
	public static void main(String...strings) {
		Cassa c = new CassaSem();
		int nrClienti = 30;
		c.test(nrClienti);
	}

}
