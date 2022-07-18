import java.util.concurrent.Semaphore;

public class AAA_BC {
	
	private static Semaphore mutex = new Semaphore(1);
	private static Semaphore A;
	private static Semaphore B = new Semaphore(0);
	private static Semaphore C = new Semaphore(0);
	private static int cntA = 0;
	private static int seqA = 10;
	
	public static void main(String...strings) {
		
		A = new Semaphore(seqA);
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

				mutex.acquire();
				cntA++;
				
				if(cntA==seqA) {
					cntA = 0;
					B.release();
					mutex.release();
				}
				else {
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
				System.out.print("B");
				C.release();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private static class C extends Thread {
		@Override public void run() {
			try { 
				C.acquire();
				System.out.println("C");
				A.release(seqA);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}