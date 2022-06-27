package ferryboat;

public class Addetto extends Thread {
	
	private FerryBoat ferryboat;
	private static int TEMPO_VIAGGIO = 20;
	
	public Addetto(FerryBoat ferryboat) {
		this.ferryboat = ferryboat;
	}
	
	@Override public void run() {
		
		try {
			
			while(true) {
				ferryboat.imbarca();
				Thread.sleep(TEMPO_VIAGGIO*10);
				ferryboat.terminaTraghettata();
			}
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override public String toString() {
		return "Addetto ";
	}

}
