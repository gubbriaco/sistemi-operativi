package PROVE3;

public class Prova333 {
	
	public static void main(String...strings) throws InterruptedException {
		MyThread[] threads = new MyThread[10];
		for(int i=0;i<threads.length;++i) {
			threads[i] = new MyThread(i);
			threads[i].start();
		}
		for(int i=threads.length-1;i>0;--i) {
			threads[i].join();
			System.out.println("T" + threads[i].id + " " + threads[i].getState());
		}
	}

	static class MyThread extends Thread {
		
		private int id;
		
		public MyThread(int id) {
			this.id = id;
		}
		
		public int getMyId() {
			return id;
		}
		
		@Override public void run() {
			try {
				System.out.println("T" + id + " " + this.getState());
				Thread.sleep(1);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
}

