package lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import defaultpackage.Buffer;
import defaultpackage.Dish;

public class BufferLockCondition extends Buffer {
	
	private Lock l = new ReentrantLock();
	private Condition possoRimuovere = l.newCondition();
	private Condition possoAggiungere = l.newCondition();
	
	private LinkedList<Thread> producerQueue;
	private LinkedList<Thread> consumerQueue;
	
	private int nrPlates;

	public BufferLockCondition(int nrDishes) {
		super(nrDishes);
		producerQueue = new LinkedList<>();
		consumerQueue = new LinkedList<>();
	}

	@Override public void put(int i) throws InterruptedException {
		l.lock();
		Thread producer = Thread.currentThread();
		try {
			producerQueue.addLast(producer);
			while(nrPlates == this.dishes.length || !producerQueue.getFirst().equals(producer) )
				possoAggiungere.await();
			
			producerQueue.removeFirst();
			nrPlates++;
			this.dishes[in] = new Dish(i);
			System.out.println(producer.toString() + " put " + this.dishes[in].toString() + " in the buffer.");
			System.out.println("After " + producer.toString() + " placed " + this.dishes[in].toString() + " in the buffer: " + this.toString());
			in = (in+1)%this.nrDishes;
			
			possoRimuovere.signalAll();
			
		}finally {
			l.unlock();
		}
	}

	@SuppressWarnings("finally")
	@Override public int get() throws InterruptedException {
		l.lock();
		Thread consumer = Thread.currentThread();
		int dishID = 0;
		try {
			consumerQueue.addLast(consumer);
			while( nrPlates == 0 || !consumerQueue.getFirst().equals(consumer) )
				possoRimuovere.await();
			
			consumerQueue.removeFirst();
			nrPlates--;
			Dish dish = this.dishes[out];
			dishID = dish.getID();
			System.out.println(consumer.toString() + " put " + this.dishes[out].toString() + " in the buffer.");
			System.out.println("After " + consumer.toString() + " consumed " + this.dishes[out].toString());
			out = (out+1)%this.nrDishes;
			
			possoAggiungere.signalAll();
			
		}finally {
			l.unlock();
			return dishID;
		}
	}
	
	
	public static void main(String...strings) {
		int bufferLength = 18;
		Buffer buffer = new BufferLockCondition(bufferLength);
		int nrProducer = 18;
		int nrConsumer = 18;
		buffer.test(nrProducer, nrConsumer);
	}

}
