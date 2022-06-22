package casa_di_cura;

public abstract class CasaDiCura {
	
	@SuppressWarnings("unused")
	private int dimensioneSalaPreparazione;
	
	public CasaDiCura(int dimensioneSalaPreparazione) {
		this.dimensioneSalaPreparazione = dimensioneSalaPreparazione;
	}
	

	public abstract void pazienteEntra() throws InterruptedException;
	
	public abstract void chiamaEIniziaOperazione() throws InterruptedException;
	
	public abstract void fineOperazione() throws InterruptedException;
	
	public abstract void pazienteEsci() throws InterruptedException;
	

	protected void test(int numeroClienti) {
		
		Thread[] pazienti = new Thread[numeroClienti];
		for(int i=0;i<numeroClienti;++i) {
			pazienti[i] = new Paziente(i, this);
			pazienti[i].start();
		}
		
		Thread medico = new Medico(this);
		medico.setDaemon(true);
		medico.start();
		
	}
	
}
