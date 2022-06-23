package semaphore;

import java.util.concurrent.Semaphore;

import defaultpackage.Banca;
import defaultpackage.Cliente;

public class BancaSemaphore extends Banca {
	
	private Semaphore mutex = new Semaphore(1);

	public BancaSemaphore(int saldoBanca) {
		super(saldoBanca);
		
	}

	@Override public int deposita(int deposito) throws InterruptedException {
		mutex.acquire();
		Cliente cliente = (Cliente)Thread.currentThread();
		int soldiAttuali = cliente.getDeposito();
		cliente.setDeposito(soldiAttuali + deposito);
		System.out.println("Il nuovo saldo del cliente " + cliente.getID() + " dopo il deposito e' " +
						   cliente.getDeposito() + " euro.");
		mutex.release();
		return cliente.getDeposito();
	}

	@Override public int preleva(int prelievo) throws InterruptedException {
		mutex.acquire();
		Cliente cliente = (Cliente)Thread.currentThread();
		int soldiAttuali = cliente.getDeposito();
		cliente.setDeposito(soldiAttuali - prelievo);
		System.out.println("Il nuovo saldo del cliente " + cliente.getID() + " dopo il prelievo e' " +
						   cliente.getDeposito() + " euro.");
		mutex.release();
		return cliente.getDeposito();
	}
	
	
	public static void main(String...strings) throws InterruptedException {
		Banca banca = new BancaSemaphore(190347);
		int numeroClienti = 8700;
		banca.test(numeroClienti);
	}

}
