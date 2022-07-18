package barbiere_addormentato.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import barbiere_addormentato.Sala;

public class SalaSem extends Sala {
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore cliente_disponibile = new Semaphore(0, true);
	private Semaphore taglio = new Semaphore(0, true);
	
	private LinkedList<Thread> in_attesa;

	public SalaSem(int numero_sedie) {
		super(numero_sedie); 
		
		cliente_disponibile = new Semaphore(numero_sedie);
		in_attesa = new LinkedList<>();
	}

	
	@Override public boolean attendi_taglio() throws InterruptedException {
		mutex.acquire();
		if(in_attesa.size() == this.getNumeroSedie()) {
			mutex.release();
			return false;
		}
		mutex.release();
		
		Thread cliente = Thread.currentThread();
		mutex.acquire();
		in_attesa.addLast(cliente);
		System.out.println("IN ATTESA: " + in_attesa.toString());
		cliente_disponibile.release();
		taglio.acquire();
		mutex.release();
		return true;
	}

	@Override public void taglia_capelli() throws InterruptedException {
		cliente_disponibile.acquire();
		taglio.release();
		
		if(in_attesa.size() != 0)
			in_attesa.removeFirst();
		System.out.println("IN ATTESA: " + in_attesa.toString());
	}
	
	
	public static void main(String...strings) {
		new SalaSem(4).test(20);
	}
	

}
