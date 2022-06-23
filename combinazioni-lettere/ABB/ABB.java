package ABB;

import java.util.concurrent.Semaphore;

public class ABB {
	
	private static Semaphore mutex = new Semaphore(1);
	private static int countB = 0;
	private static Semaphore A = new Semaphore(1);
	private static Semaphore B = new Semaphore(0);
	
	public static void main(String...strings) throws InterruptedException {
		while(true) {
			new Thread(new A()).start();
			new Thread(new B()).start();
			Thread.sleep(100);
		}
	}
	
	
	private static class A implements Runnable {
		@Override public void run() {
			try {
				A.acquire();
				System.out.print("A");
				B.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class B implements Runnable {
		@Override public void run() {
			try {
				B.acquire();
				mutex.acquire();
				countB++;
				System.out.print("B");
				if(countB == 2) {
					System.out.println();
					countB = 0;
					A.release();
				}
				mutex.release();
				B.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
