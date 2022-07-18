package lettori_scrittori.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import lettori_scrittori.MemoriaCondivisa;

public class MemoriaCondivisaSem extends MemoriaCondivisa {

	private Semaphore mutex = new Semaphore(1);
	private Semaphore lettura;
	private Semaphore lock = new Semaphore(1);
	
	private LinkedList<Thread> lettori, scrittori;
	
	public MemoriaCondivisaSem(int permessi_lettura) {
		super(permessi_lettura);
		
		lettura = new Semaphore(permessi_lettura);
		
		lettori = new LinkedList<>();
		scrittori = new LinkedList<>();
	}
	
	
	@Override public void inizio_scrittura() throws InterruptedException { 
		Thread scrittore = Thread.currentThread();
		
		lock.acquire();
		
		mutex.acquire();
		scrittori.add(scrittore);
		System.out.println("SCRITTORI: " + scrittori.toString());
		mutex.release();
	}

	@Override public void fine_scrittura() throws InterruptedException {
		Thread scrittore = Thread.currentThread();
		
		mutex.acquire();
		scrittori.remove(scrittore);
		System.out.println("SCRITTORI: " + scrittori.toString());
		mutex.release();
		
		lock.release();
	}

	@Override public void inizio_lettura() throws InterruptedException {
		Thread lettore = Thread.currentThread();
		
		lettura.acquire();
		
		mutex.acquire();
		if(lettori.size() == 0)
			lock.acquire();
		mutex.release();
		
		mutex.acquire();
		lettori.addLast(lettore);
		System.out.println("LETTORI: " + lettori.toString());
		mutex.release();
	}

	@Override public void fine_lettura() throws InterruptedException {
		Thread lettore = Thread.currentThread();
		
		mutex.acquire();
		lettori.remove(lettore);
		System.out.println("LETTORI: " + lettori.toString());
		mutex.release();
		
		if(lettori.size() == 0)
			lock.release();
		
		lettura.release();
	}
	
	
	public static void main(String...strings) {
		new MemoriaCondivisaSem(10).test(20, 4);
	}
	

}
