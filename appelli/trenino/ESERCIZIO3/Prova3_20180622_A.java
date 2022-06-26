package trenino.ESERCIZIO3;

import java.util.concurrent.Semaphore;

public class Prova3_20180622_A {

	public static Semaphore sem = new Semaphore(1);

	public static void main(String...strings) {
		
		MyThread[] threads = new MyThread[8];
		for(int i=0;i<threads.length;++i) {
			threads[i] = new MyThread(i, threads);
			threads[i].start();
		}
		
	}
	
	static class MyThread extends Thread {
		
		private int myId;
		@SuppressWarnings("unused")
		private MyThread[] threads;
		
		public MyThread(int id, MyThread[] t) {
			this.myId = id;
			this.threads = t;
		}
		
		@Override public void run() {
			try {
				
				if(myId%2==0)
					sem.acquire();
				
				System.out.println("T" + myId + " " + this.getState());
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	/**
	 * T1 RUNNABLE
	 * T7 RUNNABLE
	 * T5 RUNNABLE
	 * T0 RUNNABLE
	 * T3 RUNNABLE
	 */
	
}
