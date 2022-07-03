package Gennaio14_2022.esercizio4;

public abstract class Cassa {
	
	public Cassa() {}
	
	
	public abstract void svuotaCarrello(int N) throws InterruptedException;
	
	public abstract void scansiona() throws InterruptedException;
	
	public abstract void paga(int N) throws InterruptedException;
	
	public abstract void prossimoCliente() throws InterruptedException;
	
	
	protected void test(int nrClienti) {
		Thread[] clienti = new Cliente[nrClienti];
		for(int i=0;i<clienti.length;++i) {
			clienti[i] = new Cliente(i, this);
			clienti[i].start();
		}
		
		Thread cassiere = new Cassiere(this);
		cassiere.start();
	}

}
