package trenino;

public abstract class Trenino {
	
	public int dimensioneCabina, numeroCabine;
	
	public int[] cabine;
	
	public int scatto;
	
	public Trenino(int dimensioneCabina, int numeroCabine) {
		this.dimensioneCabina = dimensioneCabina;
		this.numeroCabine = numeroCabine;
		
		cabine = new int[numeroCabine];

		scatto = 1;
	}
	
	
	/**
	 *  Il turista vuole salire sul trenino. Si tratta di un metodo che blocca il 
	 *  turista sul punto di accesso fin quando non sale su una delle cabine del 
	 *  trenino.
	 * @throws InterruptedException
	 */
	public abstract void turistaSali() throws InterruptedException;
	
	/**
	 * Il turista scende dal trenino. Si tratta di un metodo che blocca il turista 
	 * fin quando la cabina non ritorna al punto di accesso.
	 * @throws InterruptedException
	 */
	public abstract void turistaScendi() throws InterruptedException;
	
	/**
	 * Se nella cabina che è appena arrivata al punto di accesso sono presenti dei 
	 * turisti l’impiegato fa scendere i turisti.
	 * @throws InterruptedException
	 */
	public abstract void impiegatoFaiScendere() throws InterruptedException;

	/**
	 * L’impiegato fa salire un gruppo di 10 turisti nella cabina che è arrivata al 
	 * punto di accesso.
	 * @throws InterruptedException
	 */
	public abstract void impiegatoFaiSalire() throws InterruptedException;
	
	/**
	 * L’impiegato da il comando per far fare uno scatto alle cabine del trenino.
	 * @throws InterruptedException
	 */
	public abstract void impiegatoMuovi() throws InterruptedException;
	
	
	public void test(int numeroTuristi) {

		Thread[] turisti = new Turista[numeroTuristi];
		for(int i=0;i<turisti.length;++i) {
			turisti[i] = new Turista(i+1, this);
			turisti[i].start();
		}

		Thread impiegato = new Impiegato(this);
		impiegato.start();
		
	}
	
}
