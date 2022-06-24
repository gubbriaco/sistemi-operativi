package semaphore;

import java.util.concurrent.Semaphore;

import defaultpackage.Buffer;
import defaultpackage.Dish;

public class BufferSemaphore extends Buffer {
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore ciSonoPiatti = new Semaphore(0);
	private Semaphore ciSonoPostiVuoti;
	
	
	public BufferSemaphore(int nrDishes) {
		super(nrDishes);
		ciSonoPostiVuoti = new Semaphore(nrDishes);
	}

	@Override public void put(int i) throws InterruptedException {
		ciSonoPostiVuoti.acquire();
		Thread producer = Thread.currentThread();
		
		mutex.acquire();
		this.dishes[in] = new Dish(i);
		System.out.println(producer.toString() + " put " + this.dishes[in].toString() + " in the buffer.");
		System.out.println("After " + producer.toString() + " placed " + this.dishes[in].toString() + " in the buffer: " + this.toString());
		in = (in+1)%this.nrDishes;
		mutex.release();
		
		ciSonoPiatti.release();
	}

	@Override public int get() throws InterruptedException {
		ciSonoPiatti.acquire();
		Thread consumer = Thread.currentThread();
		
		mutex.acquire();
		Dish dish = this.dishes[out];
		System.out.println(consumer.toString() + " put " + this.dishes[out].toString() + " in the buffer.");
		System.out.println("After " + consumer.toString() + " consumed " + this.dishes[out].toString());
		out = (out+1)%this.nrDishes;
		mutex.release();
		
		ciSonoPostiVuoti.release();
		return dish.getID();
	}
	
	
	public static void main(String...strings) {
		int bufferLength = 18;
		Buffer buffer = new BufferSemaphore(bufferLength);
		int nrProducer = 18;
		int nrConsumer = 18;
		buffer.test(nrProducer, nrConsumer);
	}

}
