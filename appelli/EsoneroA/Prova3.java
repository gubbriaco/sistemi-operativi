package EsoneroA;

import java.util.concurrent.TimeUnit;

public class Prova3 {
	
	public static void main(String...strings) {
		MyThread[] threads = new MyThread[6];
		for(int i=0;i<threads.length;++i)
			threads[i] = new MyThread(i,threads);
		for(int i=0;i<threads.length;++i)
			threads[i].start();
	}

}

class MyThread extends Thread {
	private int myId; private MyThread[] threads;
	public MyThread(int id, MyThread[] t) {
		this.myId = id;
		this.threads = t;
	}
	
	public void run() {
		try {
			int s = myId+1;
			if(s<threads.length)
				threads[s].join();
//			TimeUnit.SECONDS.sleep(myId);
			System.out.println("T"+myId+getState());
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
