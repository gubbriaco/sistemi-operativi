package lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import defaultpackage.Banca;
import defaultpackage.Cliente;
import semaphore.BancaSemaphore;

public class BancaLockCondition extends Banca {
	
	private Lock l = new ReentrantLock();
	private Condition possoDepositare = l.newCondition();
	private Condition possoPrelevare = l.newCondition();
	
	private LinkedList<Cliente> clienti = new LinkedList<>();

	public BancaLockCondition(int saldoBanca) {
		super(saldoBanca);
	}

	@SuppressWarnings("finally")
	@Override public int deposita(int deposito) throws InterruptedException {
		l.lock();
		Cliente cliente = (Cliente)Thread.currentThread();
		try {
			while(!cliente.equals(clienti.getFirst()))
				possoDepositare.await();
			clienti.addLast(cliente);
			int soldiAttuali = cliente.getDeposito();
			cliente.setDeposito(soldiAttuali + deposito);
			System.out.println("Il nuovo saldo del cliente " + cliente.getID() + " dopo il deposito e' " +
							   cliente.getDeposito() + " euro.");
			possoPrelevare.signalAll();
		}finally {
			l.unlock();
			return cliente.getDeposito();
		}
	}

	@SuppressWarnings("finally")
	@Override public int preleva(int prelievo) throws InterruptedException {
		l.lock();
		Cliente cliente = (Cliente)Thread.currentThread();
		try {
			while(!cliente.equals(clienti.getFirst()))
				possoPrelevare.await();
			clienti.addLast(cliente);
			int soldiAttuali = cliente.getDeposito();
			cliente.setDeposito(soldiAttuali - prelievo);
			System.out.println("Il nuovo saldo del cliente " + cliente.getID() + " dopo il prelievo e' " +
							   cliente.getDeposito() + " euro.");
			possoDepositare.signalAll();
		}finally {
			l.unlock();
			return cliente.getDeposito();
		}
	}
	
	
	public static void main(String...strings) throws InterruptedException {
		Banca banca = new BancaSemaphore(0);
		int numeroClienti = 8700;
		banca.test(numeroClienti);
	}

}
