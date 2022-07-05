package Gennaio14_2022.esercizio3;

import java.util.concurrent.Semaphore;

public class Prova3Modificata {

	public static Semaphore sem = new Semaphore(0);
	
	public static void main(String...strings) throws InterruptedException {
		MyThread[] threads = new MyThread[10];
		for(int i=0;i<threads.length;++i) {
			threads[i] = new MyThread(i, threads);
			System.out.println("T" + i + " " + threads[i].getState());
			threads[i].start();
		}
		for(int i=0;i<threads.length;++i) {
			threads[i].join();
			System.out.println("T" + i + " " + threads[i].getState());
		}
	}
	
	static class MyThread extends Thread {
		private int myId;
		private MyThread[] threads;
		
		public MyThread(int id, MyThread[] t) {
			this.myId = id;
			this.threads = t;
		}
		
		public void run() {
			try {
				Thread.sleep((long)(Math.random()*2000));
				System.out.println("T" + myId + " " + getState());
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}