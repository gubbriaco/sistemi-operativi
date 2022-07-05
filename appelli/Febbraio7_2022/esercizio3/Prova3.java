package Febbraio7_2022.esercizio3;

public class Prova3 {

	public static void main(String[] args) throws InterruptedException {
		
		MyThread ta = new MyThread("TA", new MyThread[] {});
		MyThread tb = new MyThread("TB", new MyThread[] { ta });
		MyThread tc = new MyThread("TC", new MyThread[] { tb });
		MyThread td = new MyThread("TD", new MyThread[] { tc });
		ta.start(); tb.start(); tc.start(); td.start();
		
	}
	
	static class MyThread extends Thread {
		
		private Thread[] tArray;
		
		public MyThread(String n, MyThread[] r) {
			setName(n);
			this.tArray = r;
		}
		
		public void run() {
			try {
				for(Thread thread:tArray) {
					thread.join();
				}
				System.out.println(getName());
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
