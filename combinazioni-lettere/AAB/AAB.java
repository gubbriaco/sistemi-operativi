package AAB;

import java.util.concurrent.Semaphore;

public class AAB {

	private static int countA = 0;
	private static Semaphore mutex = new Semaphore(1);
	private static Semaphore A = new Semaphore(2);
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
				mutex.acquire();
				System.out.print("A");
				countA++;
				if(countA==2)
					AAB.B.release();
				
				mutex.release();
				AAB.A.acquire();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class B implements Runnable {
		@Override public void run() {
			try {
				AAB.B.acquire();
				mutex.acquire();
				System.out.print("B");
				System.out.println();
				countA = 0;
				mutex.release();
				AAB.A.release(2);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
