package PROVE3;

public class Prova3 {
	
	public static void main(String...strings) throws InterruptedException {
		
		MyThread[] threads = new MyThread[10];
		for(int i=0;i<threads.length;++i) {
			threads[i] = new MyThread(i);
			threads[i].start();
		}
		for(int i=0;i<threads.length;++i) {
			threads[i] .join();
			System.out.println("T" + threads[i].getMyId() + " " + threads[i].getState());
		}
	}

}

class MyThread extends Thread {
	
	private int id;
	
	public MyThread(int id) {
		this.id = id;
	}
	
	public int getMyId() {
		return id;
	}
	
	@Override public void run() {
		try {
			Thread.sleep(1);
			System.out.println("T" + id + " " + this.getState());
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
