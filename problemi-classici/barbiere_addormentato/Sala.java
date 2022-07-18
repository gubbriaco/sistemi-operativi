package barbiere_addormentato;

public abstract class Sala {
	
	private int numero_sedie, numero_sedie_libere;
	
	public Sala(int numero_sedie) {
		this.numero_sedie = numero_sedie;
		this.numero_sedie_libere = numero_sedie_libere;
	}
	
	public int getNumeroSedie() {
		return numero_sedie;
	}
	
	public int getNumeroSedieLibere() {
		return numero_sedie_libere;
	}
	
	public void setNumeroSedieLibere(int numero_sedie_libere) {
		this.numero_sedie_libere = numero_sedie_libere;
	}
	
	
	public abstract boolean attendi_taglio() throws InterruptedException;
	
	public abstract void taglia_capelli() throws InterruptedException;
	
	
	
	public void test(int numero_clienti) {
		
		Thread barbiere = new Barbiere(this);
		barbiere.setDaemon(true);
		barbiere.start();
		
		Thread[] clienti = new Cliente[numero_clienti];
		for(int i=0;i<numero_clienti;++i) {
			clienti[i] = new Cliente(i, this);
			clienti[i].start();
		}
		
	}
	

}
