
import java.util.concurrent.Semaphore;

public class AABAB {
	
	private static Semaphore mutex = new Semaphore(1);
	private static Semaphore A = new Semaphore(2);
	private static Semaphore B = new Semaphore(0);
	private static int cntA = 0;
	private static boolean seq = true;
	
	public static void main(String...strings) {
		while(true) {
			new A().start();
			new B().start();
		}
	}
	
	
	private static class A extends Thread {
		@Override public void run() {
			try {
				
				A.acquire();
				System.out.print("A");
				
				mutex.acquire();
				if(seq) {
					cntA++;
					if(cntA==2) {
						cntA = 0;
						B.release();
					}
					mutex.release();
				}
				else {
					B.release();
					mutex.release();
				}
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
				if(seq) {
					System.out.print("B");
					A.release();
					seq = false;
					mutex.release();
				}
				else {
					seq = true;
					System.out.println("B");
					A.release(2);
					mutex.release();
				}
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}