package Gennaio14_2022.esercizio4;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CassaLC extends Cassa {
	
	private Lock l = new ReentrantLock(true);
	private Condition prossimo = l.newCondition();
	private Condition puoiScansionare = l.newCondition();
	private Condition puoiPagare = l.newCondition();
	private Condition cassa = l.newCondition();
	
	private LinkedList<Thread> inFila= new LinkedList<>(), prontoAPagare = new LinkedList<>();
	
	@Override public void svuotaCarrello(int N) throws InterruptedException {
		l.lock();
		try {
			
			Thread cliente = Thread.currentThread();
			inFila.add(cliente);
			System.out.println("IN FILA: " + inFila.toString());
			
			while(!possoAccedereAllaCassa(cliente))
				prossimo.await();
			
			inFila.remove(cliente);
			System.out.println("IN FILA: " + inFila.toString());
			prontoAPagare.addLast(cliente);
			System.out.println("PRONTO A PAGARE: " + prontoAPagare.toString());
			
			puoiScansionare.signal();
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoAccedereAllaCassa(Thread cliente) {
		return inFila.getFirst().equals(cliente);
	}

	@Override public void scansiona() throws InterruptedException {
		l.lock();
		try {
			
			while(!possoScansionare())
				puoiScansionare.await();
			
			puoiPagare.signalAll();
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoScansionare() {
		return prontoAPagare.size() == 1;
	}

	@Override public void paga(int N) throws InterruptedException {
		l.lock();
		try {
			
			Thread cliente = Thread.currentThread();
			
			while(!possoPagare(cliente))
				puoiPagare.await();
			
			prontoAPagare.remove(cliente);
			System.out.println("PRONTO A PAGARE: " + prontoAPagare.toString());
			
			cassa.signal();
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoPagare(Thread cliente) {
		return prontoAPagare.getFirst().equals(cliente);
	}
	
	
	@Override public void prossimoCliente() throws InterruptedException {
		l.lock();
		try {
			
			while(!prossimoClienteCassa())
				cassa.await();
			
			prossimo.signalAll();
			
			
		}finally {
			l.unlock();
		}
	}

	private boolean prossimoClienteCassa() {
		return prontoAPagare.isEmpty() && !(inFila.isEmpty());
	}
	
	
	public static void main(String...strings) {
		CassaLC c = new CassaLC();
		int nrClienti = 50;
		c.test(nrClienti);
	
	}
	
}
