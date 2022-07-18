package lettori_scrittori.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lettori_scrittori.MemoriaCondivisa;

public class MemoriaCondivisaLC extends MemoriaCondivisa {
	
	private Lock l = new ReentrantLock();
	private Condition posso_scrivere = l.newCondition();
	private boolean lock;
	private Condition posso_leggere = l.newCondition();

	private LinkedList<Thread> lettori, scrittori;
	
	public MemoriaCondivisaLC(int permessi_lettura) {
		super(permessi_lettura); 
		
		lock = false;
		
		lettori = new LinkedList<>();
		scrittori = new LinkedList<>();
	}

	@Override public void inizio_scrittura() throws InterruptedException {
		l.lock();
		Thread scrittore = Thread.currentThread();
		try {
			
			while(!posso_scrivere(scrittore))
				posso_scrivere.await();
			
			lock = true;
			scrittori.add(scrittore);
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean posso_scrivere(Thread scrittore) {
		return !lock && lettori.size() == 0 && scrittori.size() == 0;
	}
	

	@Override public void fine_scrittura() throws InterruptedException {
		l.lock();
		Thread scrittore = Thread.currentThread();
		try {

			scrittori.remove(scrittore);
			lock = false;
			System.out.println(scrittore.toString() + " ha terminato di scrivere.");
			
			posso_leggere.signalAll();
			posso_scrivere.signalAll();
			
		}finally {
			l.unlock();
		}
	}

	@Override public void inizio_lettura() throws InterruptedException {
		l.lock();
		Thread lettore = Thread.currentThread();
		try {
			
			while(!posso_leggere(lettore))
				posso_leggere.await();
			
			
			if(lettori.size() == 0)
				lock = true;
			
			lettori.addLast(lettore);
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean posso_leggere(Thread lettore) {
		if(lettori.size() == 0)
			return !lock && scrittori.size() == 0;
		else
			return lock && scrittori.size() == 0;
	}
	

	@Override public void fine_lettura() throws InterruptedException {
		l.lock();
		Thread lettore = Thread.currentThread();
		try {
			
			lettori.remove(lettore);
			System.out.println(lettore.toString() + " ha terminato di leggere.");
			
			posso_leggere.signalAll();
			
			if(lettori.size() == 0) {
				lock = false;
				posso_scrivere.signalAll();
			}
			
		}finally {
			l.unlock();
		}
	}
	
	
	public static void main(String...strings) {
		new MemoriaCondivisaLC(10).test(20, 4);;
	}
	

}
