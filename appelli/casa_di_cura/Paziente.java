package casa_di_cura;

public class Paziente extends Thread {

	private int ID;
	private CasaDiCura cdc;
	
	public Paziente(int ID, CasaDiCura cdc) {
		this.ID = ID+1;
		this.cdc = cdc;
	}
	
	
	@Override public void run() {
		
		try {
			
			cdc.pazienteEntra();
			cdc.pazienteEsci();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override public String toString() {
		return "paziente " + ID;
	}
	
	
}
