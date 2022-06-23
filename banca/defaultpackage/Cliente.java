package defaultpackage;

import java.util.Random;

public class Cliente extends Thread {
	
	private int ID;
	private Banca banca;
	private int depositoIniziale;
	private int deposito;
	
	private Random random;
	private int soldi;
	private static int MIN_SOLDI = 14000, MAX_SOLDI = 99500;
	
	public Cliente(int ID, Banca banca, int depositoIniziale) {
		this.ID = ID;
		this.banca = banca;
		this.depositoIniziale = depositoIniziale;
		deposito = depositoIniziale;
		random = new Random();
		soldi = random.nextInt(MIN_SOLDI, MAX_SOLDI+1);
	}
	
	public int getID() {
		return ID;
	}
	
	public int getDeposito() {
		return deposito;
	}
	
	public void setDeposito(int deposito) {
		this.deposito = deposito;
	}
	
	
	@Override public void run() {
		try {
			System.out.println("Il saldo del cliente " + this.ID + " prima delle operazioni e' " +
					this.deposito + " euro.");
			banca.deposita(soldi);
			banca.preleva(soldi);
			System.out.println("Il deposito finale del cliente " + ID + " e' " + deposito + " euro.");
			if(deposito == depositoIniziale)
				System.out.println("Il cliente " + ID +" e' OK!");
			else {
				System.out.println("------- ERRORE Cliente " + ID + " -------");
				System.exit(1);
			}
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
