package produttore_consumatore.semaphore;

import java.util.concurrent.Semaphore;

import produttore_consumatore.Buffer;

public class BufferSem extends Buffer {
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore ci_sono_elementi = new Semaphore(0), 
			          ci_sono_posti_vuoti = new Semaphore(1);
	
	private int in, out;
	private int[] buffer;

	public BufferSem(int capienza) {
		super(capienza);
		
		in = 0; out = 0;
		buffer = new int[capienza];
	}

	@Override public void put(int i) throws InterruptedException {
		ci_sono_posti_vuoti.acquire();
		
		mutex.acquire();
		buffer[in] = i;
		in = (in+1)%buffer.length;
		ci_sono_elementi.release();
		mutex.release();
	
	}

	@Override public int get() throws InterruptedException {
		ci_sono_elementi.acquire();
		
		mutex.acquire();
		int i = buffer[out];
		out = (out+1)%buffer.length;
		ci_sono_posti_vuoti.release();
		mutex.release();
		
		return i;
	}
	
	
	public static void main(String...strings) {
		new BufferSem(10).test(4, 20);;
	}
	

}
