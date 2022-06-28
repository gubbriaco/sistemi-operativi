package museo.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import museo.Museo;

public class MuseoLockConditionVersioneB extends Museo {
	
	private Lock l = new ReentrantLock();
	private Condition possoEntrareInArcheologica = l.newCondition();
	private Condition possoEntrareInDama = l.newCondition();
	
	private Condition possoPassareAllaProssimaSala = l.newCondition();
	private Condition possoUscireDallaDama = l.newCondition();
	
	private LinkedList<Thread> inArcheologica, inDama, gruppoVersoDama;
	
	private static int nrPersoneGruppoDama = 5;
	
	public MuseoLockConditionVersioneB(int nrPersoneArcheologica, int nrPersoneDama) {
		super(nrPersoneArcheologica, nrPersoneDama);
		
		inArcheologica = new LinkedList<>();
		inDama = new LinkedList<>();
		gruppoVersoDama = new LinkedList<>();
	}

	@Override public void visitaSA() throws InterruptedException {
		l.lock();
		Thread visitatore = Thread.currentThread();
		try {
			
			while(!possoEntrareInArcheologica(visitatore))
				possoEntrareInArcheologica.await();
			
			System.out.println("Il" + visitatore.toString() + " e' entrato nella sala archeologica.");
			inArcheologica.add(visitatore);
			System.out.println("SALA ARCHEOLOGICA: " + inArcheologica.toString());
			System.out.println("Persone nella SALA ARCHEOLOGICA: " + inArcheologica.size());
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoEntrareInArcheologica(Thread visitatore) {
		return inArcheologica.size() < this.nrPersoneArcheologica;
	}

	@Override public void terminaVisitaSA() throws InterruptedException {
		l.lock();
		Thread visitatore = Thread.currentThread();
		try {
			
			System.out.println("Il" + visitatore.toString() + " e' uscito dalla sala archeologica.");
			inArcheologica.remove(visitatore);
			System.out.println("SALA ARCHEOLOGICA: " + inArcheologica.toString());
			System.out.println("Persone nella SALA ARCHEOLOGICA: " + inArcheologica.size());
			
			possoEntrareInArcheologica.signal();
			
			while(!possoEntrareInGruppoVersoDama(visitatore))
				possoPassareAllaProssimaSala.await();
				
			gruppoVersoDama.add(visitatore);
			System.out.println("Il " + visitatore.toString() + " e' nel prossimo gruppo per entrare nela sala della dama..");
			System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.toString());
			System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.size());
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoEntrareInGruppoVersoDama(Thread visitatore) {
		return gruppoVersoDama.size() <= nrPersoneGruppoDama;
	}

	@Override public void visitaSD() throws InterruptedException {
		l.lock();
		Thread visitatore = Thread.currentThread();
		try {
			
			while(!possoEntrareInDama(visitatore))
				possoEntrareInDama.await();
			
			System.out.println("Il " + visitatore.toString() + " e' entrato nella sala della dama.");
			inDama.add(visitatore);
			System.out.println("SALA DELLA DAMA: " + inDama.toString());
			System.out.println("Persone nella SALA DELLA DAMA: " + inDama.size());
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoEntrareInDama(Thread visitatore) {
		return inDama.size() < this.nrPersoneDama && gruppoVersoDama.size() == nrPersoneGruppoDama;
	}

	@Override public void terminaVisitaSD() throws InterruptedException {
		l.lock();
		Thread visitatore = Thread.currentThread();
		try {
			
			System.out.println("Il " + visitatore.toString() + " e' uscito dalla sala della dama.");
			gruppoVersoDama.remove(visitatore);
			
			while(!possonoUscireDallaDama())
				possoUscireDallaDama.await();
			
			System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.toString());
			System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.size());
			inDama.remove(visitatore);
			System.out.println("SALA DELLA DAMA: " + inDama.toString());
			System.out.println("Persone nella SALA DELLA DAMA: " + inDama.size());
			
//			possoPassareAllaProssimaSala.signal();
//			possoEntrareInDama.signal();
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possonoUscireDallaDama() {
		return gruppoVersoDama.isEmpty();
	}
	
	
	public static void main(String...strings) {
		int nrPersoneFilaArcheologica = 40;
		int nrPersoneFilaDama = 5;
		Museo museo = new MuseoLockConditionVersioneB(nrPersoneFilaArcheologica, nrPersoneFilaDama);
		int numeroVisitatori = 50;
		museo.test(numeroVisitatori);
	}
	

}
