package esercizio4.A.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import esercizio4.Galleria;

public class GalleriaA_LC extends Galleria {
	
	private Lock l = new ReentrantLock();
	
	private Condition[] guida, posso_uscire;
	
	private Condition galleria_piena = l.newCondition(),
				      nuovo_tour = l.newCondition();
	
	private LinkedList<Thread>[] in_attesa, in_visita;
	private LinkedList<Thread> in_galleria;

	@SuppressWarnings("unchecked")
	public GalleriaA_LC(int capienza, int numero_guide, int numero_visitatori_per_guida) {
		super(capienza, numero_guide, numero_visitatori_per_guida);
		
		guida = new Condition[numero_guide];
		posso_uscire = new Condition[numero_guide];
		for(int i=0;i<numero_guide;++i) {
			guida[i] = l.newCondition();
			posso_uscire[i] = l.newCondition();
		}
		
		in_attesa = new LinkedList[numero_guide];
		in_visita = new LinkedList[numero_guide];
		for(int i=0;i<numero_guide;++i) {
			in_attesa[i] = new LinkedList<Thread>();
			in_visita[i] = new LinkedList<Thread>();
		}
		
		in_galleria = new LinkedList<>();
	}

	@Override public void iniziaVisita(int lingua) throws InterruptedException {
		l.lock(); 
		Thread visitatore = Thread.currentThread();
		try {
		
			System.out.println("IN ATTESA: " + in_attesa[lingua].toString());
			in_attesa[lingua].addLast(visitatore);
			
			while(!capienza_ok(visitatore, lingua))
				galleria_piena.await();
			
			System.out.println("IN ATTESA: " + in_attesa[lingua].toString());
			in_attesa[lingua].remove(visitatore);
			
			System.out.println("IN GALLERIA: " + in_galleria.toString());
			in_galleria.addLast(visitatore);
			
			while(!posso_visitare(lingua))
				guida[lingua].await();
			
			System.out.println("IN VISITA: " + in_visita[lingua].toString());
			in_visita[lingua].add(visitatore);
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean capienza_ok(Thread visitatore, int lingua) {
		return in_attesa[lingua].getFirst().equals(visitatore) &&
			   in_galleria.size() < this.getCapienza();
	}
	
	private boolean posso_visitare(int lingua) {
		return in_visita[lingua].size() < this.getNumeroVisitatoriPerGuida();
	}
	

	@Override public void esci(int lingua) throws InterruptedException {
		l.lock();
		Thread visitatore = Thread.currentThread();
		try {
		
			while(!posso_uscire(visitatore, lingua))
				posso_uscire[lingua].await();
			
			System.out.println("IN VISITA: " + in_visita[lingua].toString());
			in_visita[lingua].remove(visitatore);
			System.out.println("IN GALLERIA: " + in_galleria.toString());
			in_galleria.remove(visitatore);
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean posso_uscire(Thread visitatore, int lingua) {
		return in_visita[lingua].getFirst().equals(visitatore);
	}
	

	@Override public void attendiVisitatori(int lingua) throws InterruptedException {
		l.lock();
		try {
		
			while(!posso_organizzare_nuovo_tour(lingua))
				nuovo_tour.await();
			
			for(int i=0;i<this.getNumeroVisitatoriPerGuida();++i) {
				guida[lingua].signalAll();
				galleria_piena.signalAll();
			}
			
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean posso_organizzare_nuovo_tour(int lingua) {
		return in_galleria.size() < this.getCapienza() && in_visita[lingua].size()==0;
	}
	

	@Override public void terminaVisita(int lingua) throws InterruptedException {
		l.lock();
		try {
		
			for(int i=0;i<this.getNumeroVisitatoriPerGuida();++i)
				posso_uscire[lingua].signalAll();
			
		}finally {
			l.unlock();
		}
	}
	
	
	public static void main(String...strings) {
		final int CAPIENZA_GALLERIA = 200, NUMERO_GUIDE = 5, NUMERO_VISITATORI_PER_GUIDA = 20;
		Galleria galleria = new GalleriaA_LC(CAPIENZA_GALLERIA, NUMERO_GUIDE, NUMERO_VISITATORI_PER_GUIDA);
		final int NUMERO_VISITATORI = 300;
		galleria.test(NUMERO_VISITATORI);
	}
	

}
