package PROVE3;

import java.util.concurrent.Semaphore;

public class Prova33 {
	
	private static Semaphore sem = new Semaphore(4);
	
	public static void main(String...strings) {
		MyThread[] threads = new MyThread[10];
		for(int i=0;i<threads.length;++i) {
			threads[i] = new MyThread(i);
			threads[i].start();
		}
	}

	static class MyThread extends Thread {
		
		private int id;
		
		public MyThread(int id) {
			this.id = id;
		}
		
		public int getMyId() {
			return id;
		}
		
		@Override public void run() {
			try {
				if(id%2==0)
					sem.acquire(1);
				System.out.println("T" + id + " " + this.getState());
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
}

