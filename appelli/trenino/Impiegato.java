package trenino;

public class Impiegato extends Thread {
	
	private Trenino trenino;
	
	public Impiegato(Trenino trenino) {
		this.trenino = trenino;
	}
	
	
	@Override public void run() {
		
		try {
			
			trenino.impiegatoFaiScendere();
			trenino.impiegatoFaiSalire();
			trenino.impiegatoMuovi();
			
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override public String toString() {
		return "Impiegato ";
	}

}
