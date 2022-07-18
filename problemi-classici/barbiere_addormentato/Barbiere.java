package barbiere_addormentato;

public class Barbiere extends Thread {

	private Sala s;
	
	public Barbiere(Sala s) {
		this.s = s;
	}
	
	
	public void run() {
		try {
			
			s.taglia_capelli();
			
		}catch(InterruptedException e) {
			
		}
	}
	
	
}
