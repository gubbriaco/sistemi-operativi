package defaultpackage;

import java.util.Random;

public class Consumer extends Thread {
	
	private int ID;
	private Buffer buffer;
	private Random random;
	
	public Consumer(int ID, Buffer buffer) {
		this.ID = ID;
		this.buffer = buffer;
		random = new Random();
	}
	
	@Override public void run() {
		try {
			
			int i = buffer.get();
			consume(i);
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void consume(int i) throws InterruptedException {
		Thread.sleep( random.nextInt(2, i+5)*100 );
	}
	
	@Override public String toString() {
		return "Consumer " + ID; 
	}
	

}
