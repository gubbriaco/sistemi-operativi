package AAA_AB;

import java.util.concurrent.Semaphore;

public class AAA_AB {
	
	private static int countA = 5;
	private static int count = countA;
	private static Semaphore mutex = new Semaphore(countA);
	private static Semaphore A = new Semaphore(1);
	private static Semaphore B = new Semaphore(0);
	
	public static void main(String...strings) throws InterruptedException {
		while(true) {
			if(countA==0)
				System.exit(0);
			new Thread(new A()).start();
			new Thread(new B()).start();
			Thread.sleep(100);
		}
	}
	
	
	private static class A implements Runnable {
		@Override public void run() {
			try {
				A.acquire();
				mutex.acquire();
				count--;
				System.out.print("A");
				if(count == 0)
					B.release();
				A.release();
				mutex.release();
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class B implements Runnable {
		@Override public void run() {
			try {
				B.acquire();
				System.out.println("B ");
				countA--;
				count = countA;
				A.release(countA);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
