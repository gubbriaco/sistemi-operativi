package museo;

public abstract class Museo {
	
	public int nrPersoneArcheologica, nrPersoneDama;
	
	public Museo(int nrPersoneArcheologica, int nrPersoneDama) {
		this.nrPersoneArcheologica = nrPersoneArcheologica;
		this.nrPersoneDama = nrPersoneDama;
	}
	
	@Override public String toString() {
		return "Museo Czartoryski";
	}
	
	/**
	 * Sospende il visitatore fino a quando non pu&ograve entrare nella Sala Archeologica.
	 * @throws InterruptedException
	 */
	public abstract void visitaSA() throws InterruptedException;
	
	/**
	 * Il visitatore comunica che ha terminato la visita della Sala Archeologica.
	 * @throws InterruptedException
	 */
	public abstract void terminaVisitaSA() throws InterruptedException;
	
	/**
	 * Sospende il visitatore fino a quando non pu&ograve entrare nella Sala della Dama.
	 * @throws InterruptedException
	 */
	public abstract void visitaSD() throws InterruptedException;
	
	/**
	 * Il visitatore comunica che ha terminato la visita della Sala della Dama.
	 * @throws InterruptedException
	 */
	public abstract void terminaVisitaSD() throws InterruptedException;
	
	
	public void test(int numeroVisitatori) {
		
		Thread[] visitatori = new Visitatore[numeroVisitatori];
		
		for(int i=0;i<numeroVisitatori;++i) {
			visitatori[i] = new Visitatore((i+1), this);
			visitatori[i].start();
		}
		
	}

}
