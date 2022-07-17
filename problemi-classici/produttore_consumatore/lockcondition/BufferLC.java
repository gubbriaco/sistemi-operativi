package produttore_consumatore.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import produttore_consumatore.Buffer;

public class BufferLC extends Buffer {

	private Lock l = new ReentrantLock();
	private Condition buffer_pieno = l.newCondition(),
					  buffer_vuoto = l.newCondition();
	private int numero_elementi;
	
	private LinkedList<Thread> produttori, consumatori;
	
	private int in, out;
	private int[] buffer;
	
	public BufferLC(int capienza) {
		super(capienza);
		
		in = 0; out = 0;
		buffer = new int[capienza];
		numero_elementi = 0;
		
		produttori = new LinkedList<>();
		consumatori = new LinkedList<>();
	}

	@Override public void put(int i) throws InterruptedException {
		l.lock();
		Thread produttore = Thread.currentThread();
		try {
			
			produttori.addLast(produttore);
			
			while(!possoInserire(produttore))
				buffer_vuoto.await();
			
			numero_elementi++;
			buffer[in] = i;
			in = (in+1)%buffer.length;
			buffer_pieno.signalAll();
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoInserire(Thread produttore) {
		return numero_elementi < buffer.length && 
			   produttori.getFirst().equals(produttore);
	}

	@Override public int get() throws InterruptedException {
		l.lock(); 
		Thread consumatore = Thread.currentThread();
		int i = Integer.MIN_VALUE;
		try {
		
			while(!possoEstrarre(consumatore))
				buffer_pieno.await();
			
			numero_elementi--;
			i = buffer[out];
			out = (out+1)%buffer.length;
			buffer_vuoto.signalAll();
			
		}finally {
			l.unlock();
		}
		return i;
	}
	
	private boolean possoEstrarre(Thread consumatore) {
		return numero_elementi > 0 &&
			   consumatori.getFirst().equals(consumatore);
	}
	
	
	public static void main(String...strins) {
		new BufferLC(10).test(4, 20);
	}

}
