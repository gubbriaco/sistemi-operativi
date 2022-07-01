package snowboard;

public class Addetto extends Thread {
	
	private Gara g;
	
	public Addetto(Gara g) {
		this.g = g;
	}
	
	@Override public void run() {
		try {
			
		
			while(g.classifica.size() < g.N)
				g.stampaEProssimo();
				
			g.classificaFinale();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override public String toString() {
		return "Addetto";
	}

}
