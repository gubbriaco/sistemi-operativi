import java.util.concurrent.Semaphore;

public class AAB {

	private static Semaphore mutex = new Semaphore(1);
	private static Semaphore A = new Semaphore(2);
	private static Semaphore B = new Semaphore(0);
	private static int cntA = 0;
	
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
				cntA++;
				if(cntA==2)
					B.release();
				mutex.release();
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private static class B extends Thread {
		@Override public void run() {
			try { 
				B.acquire();
				System.out.println("B");
				
				mutex.acquire();
				cntA = 0;
				mutex.release();
				
				A.release(2);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}