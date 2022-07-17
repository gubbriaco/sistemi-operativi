package lettori_scrittori;

public abstract class Tavola {
	
	private int numero_posti;
	
	public Tavola(int numero_posti) {
		this.numero_posti = numero_posti;
	}
	
	public int getNumeroPosti() {
		return numero_posti;
	}
	
	
	public abstract void mangia(int i) throws InterruptedException;

	public abstract void pensa(int i) throws InterruptedException;
	
	
	public void test(int numero_filosofi) {
		Thread[] filosofi = new Filosofo[numero_filosofi];
		for(int i=0;i<numero_filosofi;++i) {
			filosofi[i] = new Filosofo(i, this);
			filosofi[i].start();
		}
	}
	
}
