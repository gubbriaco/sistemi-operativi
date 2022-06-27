package ferryboat;

public abstract class FerryBoat {
	
	@SuppressWarnings("unused")
	public int capienza;
	
	public FerryBoat(int capienza) {
		this.capienza = capienza;
	}
	
	/**
	 * Permette ad un'auto di imbarcarsi. L'auto si deve mettere in fila (FIFO) e
	 * attendere che l'addetto gli dia il permesso di salire.
	 * @throws InterruptedException
	 */
	public abstract void sali() throws InterruptedException;
	
	/**
	 * Permette all'auto di parcheggiarsi all'interno del ferry-boat e di attendere
	 * di scendere (LIFO) quando il viaggio termina.
	 * @throws InterruptedException
	 */
	public abstract void parcheggiaEScendi() throws InterruptedException;
	
	/**
	 * Permette all'addetto di imbarcare un'auto e attendere che questa parcheggi.
	 * Finch&egrave l'auto non ha completato il parcheggio l'addetto non pu&ograve
	 * far entrare un'altra auto.
	 * @throws InterruptedException
	 */
	public abstract void imbarca() throws InterruptedException;
	
	/**
	 * Permette all'addetto di terminare il viaggio e far scendere la prima auto 
	 * dal ferry-boat (LIFO)
	 * @throws InterruptedException
	 */
	public abstract void terminaTraghettata() throws InterruptedException;
	
	
	@Override public String toString() {
		return "Ferry-Boat";
	}

	public void test(int numeroAuto) {
		
		Thread[] auto = new Auto[numeroAuto];
		for(int i=0;i<numeroAuto;++i) {
			auto[i] = new Auto(i+1, this);
			auto[i].start();
		}
		
		Thread addetto = new Addetto(this);
		addetto.start();
			
	}
	
}
