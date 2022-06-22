package casa_di_cura;

public class Medico extends Thread {
	
	private CasaDiCura cdc;
	
	public Medico(CasaDiCura cdc) {
		this.cdc = cdc;
	}
	
	
	@Override public void run() {
		
		try {
			
			cdc.chiamaEIniziaOperazione();
			cdc.fineOperazione();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override public String toString() {
		return "medico ";
	}
	

}
