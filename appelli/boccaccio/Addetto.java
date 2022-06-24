package boccaccio;

public class Addetto extends Thread {
	
	private Boccaccio boccaccio;
	
	public Addetto(Boccaccio boccaccio) {
		this.boccaccio = boccaccio;
	}
	
	@Override public void run() {
		try {
			
			boccaccio.riempi();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override public String toString() {
		return "Addetto ";
	}

}
