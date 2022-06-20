package bar_mod;

public abstract class Bar {

	protected static final int CASSA = 0, BANCONE = 1;
	protected static final int NUMERO_FILE = 2;
	
	protected static final int[] MAX_PERSONE_PER_FILA = {1, 4};
	
	protected int[] numeroPostiLiberi = new int[NUMERO_FILE];
	
	public Bar() {
		for(int i=0;i<NUMERO_FILE;++i)
			numeroPostiLiberi[i] = MAX_PERSONE_PER_FILA[i];
	}
	

	public abstract int scegli() throws InterruptedException;
	
	public abstract void inizia(int i) throws InterruptedException;
	
	public abstract void finisci(int i) throws InterruptedException;
	
	
	protected void test(int numeroClienti) {
		
		Thread[] persone = new Persona[numeroClienti];
		for(int i=0;i<numeroClienti;++i) {
			persone[i] = new Persona(i, this);
			persone[i].start();
		}
		
	}
	
}
