package laghetto;

public abstract class Laghetto {
	
	protected static int MIN_PESCI;
	private static int MAX_PESCI;
	protected int nrPescatori;
	private int nrAddetti;
	
	public Laghetto(int minPesci, int maxPesci, int nrPescatori, int nrAddetti) {
		MIN_PESCI = minPesci;
		MAX_PESCI = maxPesci;
		this.nrPescatori = nrPescatori;
		this.nrAddetti = nrAddetti;
	}
	
	@Override public String toString() {
		return "laghetto";
	}
	
	
	public abstract void inizia(int t) throws InterruptedException;
	
	public abstract void finisci(int t) throws InterruptedException;
	
	
	public void test() {
		
		Thread[] pescatori = new Pescatore[nrPescatori];
		for(int i=0;i<nrPescatori;++i) {
			pescatori[i] = new Pescatore(i, this);
			pescatori[i].start();
		}
		
		Thread[] addetti = new Addetto[nrAddetti];
		for(int i=0;i<nrAddetti;++i) {
			addetti[i] = new Addetto(i, this);
			addetti[i].start();
		}
		
	}
	

}
