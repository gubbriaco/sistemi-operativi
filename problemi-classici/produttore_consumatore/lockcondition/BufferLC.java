package produttore_consumatore.lockcondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import produttore_consumatore.Buffer;

public class BufferLC extends Buffer {

	private Lock l = new ReentrantLock();
	private Condition ci_sono_elementi = l.newCondition(),
					  ci_sono_posti_vuoti = l.newCondition();
	
	private int in, out;
	private int[] buffer;
	
	public BufferLC(int capienza) {
		super(capienza);
		
		in = 0; out = 0;
		buffer = new int[capienza];
	}

	@Override public void put(int i) throws InterruptedException {
		l.lock();
		try {
			
			while(pieno())
				ci_sono_posti_vuoti.await();
			
			buffer[in] = i;
			in = (in+1)%buffer.length;
			ci_sono_elementi.signalAll();
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean pieno() {
		return buffer.length == this.getCapienza();
	}

	@Override public int get() throws InterruptedException {
		l.lock(); 
		int i = Integer.MIN_VALUE;
		try {
		
			while(vuoto())
				ci_sono_elementi.await();
			
			i = buffer[out];
			out = (out+1)%buffer.length;
			ci_sono_posti_vuoti.signalAll();
			
		}finally {
			l.unlock();
		}
		return i;
	}
	
	private boolean vuoto() {
		return buffer.length == 0;
	}
	
	
	public static void main(String...strins) {
		new BufferLC(10).test(4, 20);
	}

}
