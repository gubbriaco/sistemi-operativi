package ABC;

import java.util.concurrent.Semaphore;

public class ABC {

	private static Semaphore A = new Semaphore(1);
	private static Semaphore B = new Semaphore(0);
	private static Semaphore C = new Semaphore(0);
	
	public static void main(String...strings) throws InterruptedException {
		while(true) {
			new Thread(new A()).start();
			new Thread(new B()).start();
			new Thread(new C()).start();
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
				System.out.print("B");
				C.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class C implements Runnable {
		@Override public void run() {
			try {
				C.acquire();
				System.out.println("C");
				A.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
