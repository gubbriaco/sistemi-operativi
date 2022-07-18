package lettori_scrittori;

public class Scrittore extends Thread {
	
	private int id;
	private MemoriaCondivisa mc;
	
	public Scrittore(int id, MemoriaCondivisa mc) {
		this.id = id;
		this.mc = mc;
	}
	
	@Override public String toString() {
		return "Scrittore " + id;
	}
	
	
	@Override public void run() {
		try {
			
			mc.inizio_scrittura();
			System.out.println(this.toString() + " sta scrivendo..");
			mc.fine_scrittura();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
