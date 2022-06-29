package centro_storico;

public abstract class TourFirenze {
	
	public int nr_turisti_per_gruppo;
	
	public int nr_turisti;
	
	public TourFirenze(int nr_turisti_per_gruppo) {
		this.nr_turisti_per_gruppo = nr_turisti_per_gruppo;
	}
	
	/**
	 * La guida attende la formazione del gruppo di turisti;
	 * @throws InterruptedException
	 */
	public abstract void attendiFormazioneGruppo() throws InterruptedException;
	
	/**
	 * La guida inizia il tour a piedi (avvisa i turisti dell'inizio del tour).
	 * @throws InterruptedException
	 */
	public abstract void visitaInizia() throws InterruptedException;
	
	/**
	 * La guida finisce la visita della citt&agrave.
	 * @throws InterruptedException
	 */
	public abstract void visitaFine() throws InterruptedException;
	
	/**
	 * Il turista attende l'inizio del tour. Si tratta di un metodo che blocca il
	 * turista fin quando non viene autorizzato dalla guida.
	 * @throws InterruptedException
	 */
	public abstract void turistaInizia() throws InterruptedException;
	
	/**
	 * Il turista finisce la visita. Il turista rimane bloccato fin quando la guida
	 * non finisce il tour.
	 * @throws InterruptedException
	 */
	public abstract void turistaFine() throws InterruptedException;
	
	
	public void test(int nr_turisti) {
		
		this.nr_turisti = nr_turisti;
		
		Thread guida = new Guida(this);
		guida.start();
		
		Thread[] turisti = new Turista[nr_turisti];
		for(int i=0;i<nr_turisti;++i) {
			turisti[i] = new Turista((i+1), this);
			turisti[i].start();
		}
	
		
	}
	

}
