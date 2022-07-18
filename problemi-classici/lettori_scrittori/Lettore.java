package lettori_scrittori;

public class Lettore extends Thread {
	
	private int id;
	private MemoriaCondivisa mc;
	
	public Lettore(int id, MemoriaCondivisa mc) {
		this.id = id;
		this.mc = mc;
	}
	
	@Override public String toString() {
		return "Lettore " + id;
	}
	
	
	@Override public void run() {
		try {
			
			mc.inizio_lettura();
			System.out.println(this.toString() + " sta leggendo..");
			mc.fine_lettura();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
