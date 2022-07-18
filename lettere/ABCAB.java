import java.util.concurrent.Semaphore;

public class ABCAB {

	private static Semaphore mutex = new Semaphore(1);
	private static Semaphore A = new Semaphore(1);
	private static Semaphore B = new Semaphore(0);
	private static Semaphore C = new Semaphore(0);
	private static boolean abc = true;
	
	public static void main(String...strings) {
		while(true) {
			new A().start();
			new B().start();
			new C().start();
		}
	}
	
	
	private static class A extends Thread {
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
	private static class B extends Thread {
		@Override public void run() {
			try { 
				B.acquire();
				
				mutex.acquire();
				if(abc) {
					System.out.print("B");
					abc = false;
					C.release();
					mutex.release();
				}
				else {
					System.out.println("B");
					abc = true;
					A.release();
					mutex.release();
				}
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private static class C extends Thread {
		@Override public void run() {
			try { 
				C.acquire();
				System.out.print("C");
				A.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}