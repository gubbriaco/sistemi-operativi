package Giugno16_2021.esercizio3;

import java.util.concurrent.Semaphore;

public class Prova3_20210616_B {
	
	public static int var1 = 0, var2 = 0;
	public static Semaphore mutex = new Semaphore(1);
	
	
	public static void main(String[] args) throws InterruptedException {
		
		MyThread[] threads = new MyThread[7];
		for(int i=0;i<7;i++) {
			threads[i] = new MyThread();
			threads[i].start();
		}
		for(int i=0;i<7;i++) {
			threads[i].join();
		}
		System.out.println(Thread.currentThread().getName()+" "+Thread.currentThread().getState());
		System.out.println(var1+" "+var2);
	
	}
	
	static class MyThread extends Thread {
		
		public void run() {
			try {
				mutex.acquire();
				var1 = var1 +1;
				mutex.release();
				var2 = var2 +1;
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
