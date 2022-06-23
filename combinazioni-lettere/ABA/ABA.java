package ABA;

import java.util.concurrent.Semaphore;

public class ABA {
	
	private static Semaphore mutex = new Semaphore(1);
	private static Semaphore A = new Semaphore(1);
	private static Semaphore B = new Semaphore(0);
	private static int countA = 0;
	
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
				ABA.A.acquire();
				mutex.acquire();
				countA++;
				if(countA == 2) {
					System.out.println("A");
					countA = 0;
					ABA.A.release();
					mutex.release();
				}
				else {
					System.out.print("A");
					ABA.B.release();
					mutex.release();
				}
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class B implements Runnable {
		@Override public void run() {
			try {
				ABA.B.acquire();
				mutex.acquire();
				System.out.print("B");
				mutex.release();
				ABA.A.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
