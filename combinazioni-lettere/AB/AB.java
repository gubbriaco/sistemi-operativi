package AB;

import java.util.concurrent.Semaphore;

public class AB {
	
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
				
				AB.A.acquire();
				System.out.print("A");
				AB.B.release();
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class B implements Runnable {
		@Override public void run() {
			try {
				
				AB.B.acquire();
				System.out.print("B");
				System.out.println();
				AB.A.release();
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

}
