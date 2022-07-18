package lettori_scrittori;

public abstract class MemoriaCondivisa {
	
	private int permessi_lettura;
	
	public MemoriaCondivisa(int permessi_lettura) {
		this.permessi_lettura = permessi_lettura;
	}
	
	public abstract void inizio_scrittura() throws InterruptedException;
	
	public abstract void fine_scrittura() throws InterruptedException;
	
	public abstract void inizio_lettura() throws InterruptedException;
	
	public abstract void fine_lettura() throws InterruptedException;
	
	
	public void test(int numero_lettori, int numero_scrittori) {
		
		Thread[] lettori = new Lettore[numero_lettori];
		for(int i=0;i<numero_lettori;++i) {
			lettori[i] = new Lettore(i, this);
			lettori[i].start();
		}
		
		Thread[] scrittori = new Scrittore[numero_scrittori];
		for(int i=0;i<numero_scrittori;++i) {
			scrittori[i] = new Scrittore(i, this);
			scrittori[i].start();
		}
		
	}

}
