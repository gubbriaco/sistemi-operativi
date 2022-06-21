package azienda_agricola;

public abstract class AziendaAgricola {

	protected int[] buffer;
	protected int in, out;
	
	protected int sacchettiDisponibili;
	protected int incasso;
	
	protected static int COSTO_SACCHETTO = 3;
	
	public AziendaAgricola(int numeroSacchetti) {
		buffer = new int[numeroSacchetti];
		in = 0;
		out = 0;
		sacchettiDisponibili = numeroSacchetti;
		incasso = 0;
	}
	
	public abstract void pagamento(int numeroSacchetti) throws InterruptedException;
	
	public abstract void ritira() throws InterruptedException;
	
	public abstract void carica() throws InterruptedException;
	
	
	public void test(int numeroClienti) throws InterruptedException {
		
		Thread magazziniere = new Magazziniere(this);
		magazziniere.setDaemon(true);
		magazziniere.start();
		
		Thread[] clienti = new Cliente[numeroClienti];
		for(int i=0;i<numeroClienti;++i) {
			clienti[i] = new Cliente(this, i);
			clienti[i].start();
		}
		
		for(int i=0;i<numeroClienti;++i)
			clienti[i].join();
		
		System.out.println("******* L'azienda agricola ha incassato " + incasso + " euro *******");
	
	}
	
	
}
