package boccaccio;

public class Bambino extends Thread {
	
	private int ID;
	private Boccaccio boccaccio;
	private int colore;
	
	public Bambino(int ID, Boccaccio boccaccio, int colore) {
		this.ID = ID;
		this.boccaccio = boccaccio;
		this.colore = colore;
	}
	
	@Override public void run() {
		try {
			
			boolean preso = boccaccio.prendi(colore);
	        if(!preso) {
	        	boccaccio.piangi();
	        	boccaccio.prendi(colore);
	        }
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override public String toString() {
		return "Bambino " + ID;
	}

}
