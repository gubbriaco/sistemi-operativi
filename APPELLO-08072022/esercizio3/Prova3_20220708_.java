package esercizio3;

import java.lang.Thread.State;

public class Prova3_20220708_ {

	public static void main(String...strings) throws InterruptedException {
		
		Thread t1 = new Thread(new MyThread());
		t1.start();
		t1.join();
		System.out.printf("%s\n", t1.getState().equals(State.RUNNABLE));
		Thread t2 = new Thread(new MyThread());
		t2.start();
		System.out.printf("%s, %s", t2.getState(), Thread.currentThread().getState());
		
	}
	
	static class MyThread implements Runnable {
		
		public void run() {
			try {
				Thread.sleep(500);
			}catch(InterruptedException e) { }
		}
		
	}
	
}
