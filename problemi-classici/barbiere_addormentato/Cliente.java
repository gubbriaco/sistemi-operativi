package barbiere_addormentato;

public class Cliente extends Thread {
	
	private int id;
	private Sala s;
	
	public Cliente(int id, Sala s) {
		this.id = id;
		this.s = s;
	}
	
	@Override public String toString() {
		return "Cliente " + id;
	}
	
	
	@Override public void run() {
		try {
			
			s.attendi_taglio();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
