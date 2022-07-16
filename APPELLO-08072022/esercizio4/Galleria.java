package esercizio4;

import java.util.Random;

public abstract class Galleria {
	
	private int capienza, numero_guide, numero_visitatori, numero_visitatori_per_guida;
	
	public Galleria(int capienza, int numero_guide, int numero_visitatori_per_guida) {
		this.capienza = capienza;
		this.numero_guide = numero_guide;
		this.numero_visitatori_per_guida = numero_visitatori_per_guida;
	}
	
	public int getCapienza() {
		return capienza;
	}
	
	public int getNumeroVisitatoriPerGuida() {
		return numero_visitatori_per_guida;
	}
	
	@Override public String toString() {
		return "Galleria degli Uffizi";
	}
	
	
	/**
	 * Permette al visitatore di visitare la Galleria, mettendosi in coda per
	 * la guida della propria lingua (in ordine FIFO).
	 * @param lingua
	 * @throws InterruptedException
	 */
	public abstract void iniziaVisita(int lingua) throws InterruptedException;
	
	/**
	 * Permette al visitatore di attendere la fine della visita guidata con la 
	 * propria guida ed uscire dalla Galleria.
	 * @param lingua
	 * @throws InterruptedException
	 */
	public abstract void esci(int lingua) throws InterruptedException;
	
	/**
	 * Permette alla guida di avvisare i visitatori della sua presenza e di 
	 * attendere la formazione del gruppo per iniziare la visita (attesa di 
	 * 20 minuti). Una volta iniziata la visita i nuovi visitatori verranno
	 * sospesi.
	 * @param lingua
	 * @throws InterruptedException
	 */
	public abstract void attendiVisitatori(int lingua) throws InterruptedException;
	
	/**
	 * Permette alla guida di portare i visitatori all'uscita e terminare la 
	 * visita. Una volta terminata una visita la guida si riposa.
	 * @param lingua
	 * @throws InterruptedException
	 */
	public abstract void terminaVisita(int lingua) throws InterruptedException;
	
	
	public void test(int numero_visitatori) {
		
		Thread[] guide = new Guida[numero_guide];
		for(int i=0;i<guide.length;++i) {
			guide[i] = new Guida(i, this);
			guide[i].start();
		}
		

		final int MIN_LINGUA = 0, MAX_LINGUA = numero_guide;
		Random random = new Random();
		
		this.numero_visitatori = numero_visitatori;
		Thread[] visitatori = new Visitatore[numero_visitatori];
		for(int i=0;i<visitatori.length;++i) {
			int lingua = random.nextInt(MIN_LINGUA, MAX_LINGUA);
			visitatori[i] = new Visitatore(i, lingua, this);
			visitatori[i].start();
		}
		
	}
	
	public int getNumeroGuide() {
		return numero_guide;
	}
	
	public int getNumeroVisitatori() {
		return numero_visitatori;
	}
	
	
}
