package produttore_consumatore;

public abstract class Buffer {
	
	private int capienza;
	
	public Buffer(int capienza) {
		this.capienza = capienza;
	}
	
	public int getCapienza() {
		return capienza;
	}
	
	
	public abstract void put(int i) throws InterruptedException;
	
	public abstract int get() throws InterruptedException;
	
	
	public void test(int numero_produttori, int numero_consumatori) {
		Thread[] produttori = new Produttore[numero_produttori];
		for(int i=0;i<numero_produttori;++i) {
			produttori[i] = new Produttore(i, this);
			produttori[i].start();
		}
		
		Thread[] consumatori = new Consumatore[numero_consumatori];
		for(int i=0;i<numero_consumatori;++i) {
			consumatori[i] = new Consumatore(i, this);
			consumatori[i].start();
		}
		
	}

}
