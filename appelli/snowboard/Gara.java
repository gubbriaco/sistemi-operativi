package snowboard;

import java.util.LinkedList;

public abstract class Gara {
	
	public int N;
	public LinkedList<Snowboarder> classifica;
	public LinkedList<Snowboarder> inAttesaDiPartire;

	
	public Gara(int N) {
		this.N = N;
		
		inAttesaDiPartire = new LinkedList<>();
		classifica = new LinkedList<>();
	}
	
	
	/**
	 * Sospende lo {@link Snowboarder} s fin quando non &egrave il suo turno
	 * (ordine FIFO di arrivo) e la pista non &egrave libera.
	 * @param s
	 * @throws InterruptedException
	 */
	public abstract void partenza(Snowboarder s) throws InterruptedException;
	
	/**
	 * Permette allo {@link Snowboarder} s di tagliare il traguardo.
	 * @param s
	 * @return
	 * @throws InterruptedException
	 */
	public abstract int arrivo(Snowboarder s) throws InterruptedException;
	
	/**
	 * L'{@link Addetto} stampa le informazioni riguardo l'ultimo {@link Snowboarder}
	 * che ha usato la pista (tempo di discesa e posizione temporanea dello 
	 * {@link Snowboarder}) e fa partire il prossimo {@link Snowboarder}. Quando gli
	 * {@link Snowboarder}s sono terminati restituisce false.
	 * @return
	 * @throws InterruptedException
	 */
	public abstract boolean stampaEProssimo() throws InterruptedException;
	
	/**
	 * L'{@link Addetto} stampa su schermo la classifica finale.
	 * @throws InterruptedException
	 */
	public abstract void classificaFinale() throws InterruptedException;
	
	
	
	public void test() {
		
		Thread addetto = new Addetto(this);
		addetto.start();
		
		Thread[] snowboarders = new Snowboarder[N];
		for(int i=0;i<N;++i) {
			snowboarders[i] = new Snowboarder(i, this);
			snowboarders[i].start();
		}
		
	}

}
