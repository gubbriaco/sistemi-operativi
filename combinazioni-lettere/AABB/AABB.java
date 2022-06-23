package AABB;

import java.util.concurrent.Semaphore;

public class AABB {

	private static Semaphore mutex = new Semaphore(1);
	private static Semaphore A = new Semaphore(2);
	private static Semaphore B = new Semaphore(0);
	private static int countA = 0;
	private static int countB = 0;
	
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
				AABB.A.acquire();
				mutex.acquire();
				System.out.print("A");
				countA++;
				if(countA == 2) {
					countB = 0;
					AABB.B.release(2);
				}
				mutex.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class B implements Runnable {
		@Override public void run() {
			try {
				AABB.B.acquire();
				mutex.acquire();
				System.out.print("B");
				countB++;
				if(countB == 2) {
					countA = 0;
					AABB.A.release(2);
					System.out.println();
				}
				mutex.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
