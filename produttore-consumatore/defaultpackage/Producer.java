package defaultpackage;

import java.util.Random;

public class Producer extends Thread {
	
	private int ID;
	private Buffer buffer;
	private Random random;
	
	public Producer(int ID, Buffer buffer) {
		this.ID = ID;
		this.buffer = buffer;
		random = new Random();
	}
	
	
	@Override public void run() {
		try {
			
			int id = random.nextInt(0, buffer.nrDishes);
			produce(id);
			buffer.put(id);
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void produce(int id) throws InterruptedException {
		Thread.sleep(random.nextInt(2, id+8)*100);
	}
	
	@Override public String toString() {
		return "Producer " + ID;
	}

}
