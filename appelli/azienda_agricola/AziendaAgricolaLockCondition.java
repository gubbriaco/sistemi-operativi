package azienda_agricola;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AziendaAgricolaLockCondition extends AziendaAgricola {

	 private Lock l=new ReentrantLock();
	 
	 private Condition possoPagare = l.newCondition();
	 private Condition possoPrelevareSacco = l.newCondition();
	
	 private Condition possoCaricareSacchi = l.newCondition();
	 
	 private LinkedList<Thread> codaCassa;
	 private LinkedList<Thread> codaMagazzino;
	
	
	public AziendaAgricolaLockCondition(int numeroSacchetti) {
		super(numeroSacchetti);
		
		codaCassa = new LinkedList<Thread>();
		codaMagazzino = new LinkedList<Thread>();
	}

	
	@Override public void pagamento(int numeroSacchetti) throws InterruptedException {
		l.lock();
		Thread current = Thread.currentThread();
		
		try {
			
			/** si mette in fila alla cassa */
			codaCassa.add(current);
			
			/** fin quando sto in fila allora deve aspettare per pagare */
			while( !codaCassa.getFirst().equals(current) )
				possoPagare.await();
			
			/** non e' piu' in fila ma sta pagando */
			codaCassa.removeFirst();
			
			/** calcolo l'incasso nuovo */
			incasso = incasso + (numeroSacchetti*COSTO_SACCHETTO);
			
			/** avanti il prossimo a pagare */
			possoPagare.signalAll();
			
		}finally {
			l.unlock();
		}
		
	}

	@Override public void ritira() throws InterruptedException {
		l.lock();
		Thread current = Thread.currentThread();
		
		try {
			
			/** si mette in fila per ritirare i sacchi */
			codaMagazzino.add(current);
			
			/** se e' ancora in coda o i sacchetti disponibili sono finiti allora
			 *  deve aspettare */
			while( !codaMagazzino.getFirst().equals(current) || sacchettiDisponibili==0)
				possoPrelevareSacco.await();
			
			/** e' il suo turno quindi non e' piu' in coda */
			codaMagazzino.removeFirst();
			sacchettiDisponibili = sacchettiDisponibili-1;
			
			if( sacchettiDisponibili==0 )
				possoCaricareSacchi.signal();
			
			/** avanti il prossimo per ritirare i sacchi */
			possoPrelevareSacco.signalAll();
			
		}finally {
			l.unlock();
		}
		
	}

	@Override public void carica() throws InterruptedException {
		l.lock();
		
		try {
			
			while( sacchettiDisponibili>0 )
				possoCaricareSacchi.await();
			
			sacchettiDisponibili = buffer.length;
			
			possoPrelevareSacco.signalAll();
			
		}finally {
			l.unlock();
		}
	}

	
	public static void main(String...strings) throws InterruptedException {
		
		AziendaAgricola aa = new AziendaAgricolaLockCondition(200);
		aa.test(100);
		
	}
	
}
