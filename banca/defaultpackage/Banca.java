package defaultpackage;

public abstract class Banca {
	
	private int saldoIniziale, saldoBanca;
	
	public Banca(int saldoBanca) {
		this.saldoIniziale = saldoBanca;
		this.saldoBanca = saldoIniziale;
	}
	
	public int getSaldoIniziale(){
		return saldoIniziale;
	}
	
	public int getSaldoBanca() {
		return saldoBanca;
	}
	
	public abstract int deposita(int deposito) throws InterruptedException;
	
	public abstract int preleva(int prelievo) throws InterruptedException;
	
	
	public void test(int numeroClienti) throws InterruptedException {
		System.out.println("******* Il saldo iniziale della banca e' " + this.getSaldoBanca() + 
				" euro *******");
		Thread[] clienti = new Cliente[numeroClienti];
		for(int i=0;i<numeroClienti;++i) {
			clienti[i] = new Cliente(i+1, this, 100000);
			clienti[i].start();
		}
		
		for(int i=0;i<numeroClienti;++i)
			clienti[i].join();
		
		if(saldoIniziale == saldoBanca) {
			System.out.println("******* Il saldo finale della banca e' " + this.getSaldoBanca() + 
				" euro *******");
		}
		else
			System.out.println(" *** ERRORE BANCA - SALDO NON VALIDO *** " + 
		" SALDO INIZIALE = "+ saldoIniziale + " euro " + " SALDO FINALE = " + saldoBanca + " euro");
	}

}
