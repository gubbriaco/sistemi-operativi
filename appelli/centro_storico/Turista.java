package centro_storico;

public class Turista extends Thread {
	
	private int id;
	private TourFirenze tf;
	
	public Turista(int id, TourFirenze tf) {
		this.id = id;
		this.tf = tf;
	}
	
	
	@Override public void run() {
		try {
			
			tf.turistaInizia();
			
			tf.turistaFine();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override public String toString() {
		return "Turista " + id;
	}

}
