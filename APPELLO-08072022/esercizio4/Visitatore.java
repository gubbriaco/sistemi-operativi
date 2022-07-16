package esercizio4;

public class Visitatore extends Thread {

	private int id;
	private Galleria galleria;
	private int lingua;
	
	public Visitatore(int id, int lingua, Galleria galleria) {
		this.id = id;
		this.galleria = galleria;
		this.lingua = lingua;
	}
	
	@Override public String toString() {
		return "Visitatore " + id;
	}
	
	
	@Override public void run() {
		try {
	
			System.out.println(this.toString() + " intende visitare la " + 
									galleria.toString());
			galleria.iniziaVisita(lingua);
			
			System.out.println(this.toString() + " ha iniziato la visita presso la "
					               + galleria.toString());
			System.out.println(this.toString() + " sta visitando la " + 
					                galleria.toString() + "..");
			
			galleria.esci(lingua);
			System.out.println(this.toString() + " ha terminato la visita presso la "
								+ galleria.toString());
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
