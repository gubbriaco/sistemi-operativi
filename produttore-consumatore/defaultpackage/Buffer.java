package defaultpackage;

import java.util.Arrays;

public abstract class Buffer {
	
	public Dish[] dishes;
	public int nrDishes;
	public int in, out;
	
	public Buffer(int nrDishes) {
		dishes = new Dish[nrDishes];
		this.nrDishes = nrDishes;
		in = 0;
		out = 0;
	}
	
	
	public abstract void put(int i) throws InterruptedException;
	
	public abstract int get() throws InterruptedException;
	
	
	@Override public String toString() {
		return Arrays.toString(dishes);
	}
	
	
	public void test(int nrProducer, int nrConsumer) {
		
		if(nrConsumer > nrProducer)
			throw new IllegalArgumentException("Illegal input test.");
		
		Thread[] producers = new Producer[nrProducer];
		Thread[] consumers = new Consumer[nrConsumer];
		
		for(int i=0;i<nrProducer;++i) {
			producers[i] = new Producer(i+1, this);
			producers[i].start();
		}
		
		for(int i=0;i<nrConsumer;++i) {
			consumers[i] = new Consumer(i+1 ,this);
			consumers[i].start();
		}
		
	}
	

}
