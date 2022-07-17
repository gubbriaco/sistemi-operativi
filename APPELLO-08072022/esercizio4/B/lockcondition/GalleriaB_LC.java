package esercizio4.B.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import esercizio4.Galleria;

public class GalleriaB_LC extends Galleria{

	private Lock l = new ReentrantLock();
	
	private Condition[] guida, posso_uscire;
	private Condition galleria_piena = l.newCondition(),
				      nuovo_tour = l.newCondition();
	
	private LinkedList<Thread>[] in_attesa, in_visita;
	private LinkedList<Thread> in_galleria;
	
	private int numero_sale, numero_visitatori_per_sala;
	private LinkedList<Thread> sala1, sala2, sala3;
	private Condition entrata_sala1 = l.newCondition(), 
			          entrata_sala2 = l.newCondition(), 
			          entrata_sala3 = l.newCondition();
	
	public GalleriaB_LC(int capienza, int numero_sale, int numero_visitatori_per_sala,
			int numero_guide, int numero_visitatori_per_guida) {
		super(capienza, numero_guide, numero_visitatori_per_guida);
		
		this.numero_sale = numero_sale;
		this.numero_visitatori_per_sala = numero_visitatori_per_sala;
		
		
		sala1 = new LinkedList<>();
		sala2 = new LinkedList<>();
		sala3 = new LinkedList<>();
		
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
			
			
			while(!posso_entrare_sala1(visitatore))
				entrata_sala1.await();
			sala1.addLast(visitatore);
			System.out.println(sala1.toString());
			
			while(!posso_entrare_sala2(visitatore))
				entrata_sala2.await();
			sala1.remove(visitatore); System.out.println(sala1.toString());
			sala2.addLast(visitatore);
			System.out.println(sala2.toString());
			
			while(!posso_entrare_sala3(visitatore))
				entrata_sala3.await();
			sala2.remove(visitatore); System.out.println(sala2.toString());
			sala3.addLast(visitatore);
			System.out.println(sala3.toString());
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean posso_entrare_sala1(Thread visitatore) {
		return sala1.size() < this.numero_visitatori_per_sala &&
			   in_galleria.contains(visitatore);	
	}
	
	private boolean posso_entrare_sala2(Thread visitatore) {
		return sala2.size() < this.numero_visitatori_per_sala &&
			   in_galleria.contains(visitatore);	
	}
	
	private boolean posso_entrare_sala3(Thread visitatore) {
		return sala3.size() < this.numero_visitatori_per_sala &&
			   in_galleria.contains(visitatore);	
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
			
			sala3.remove(visitatore);
			System.out.println(sala3.toString());
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
			
			while(sala1.size() < this.numero_visitatori_per_sala)
				entrata_sala1.notifyAll();
			
			while(sala2.size() < this.numero_visitatori_per_sala)
				entrata_sala2.notifyAll();
			
			while(sala3.size() < this.numero_visitatori_per_sala)
				entrata_sala3.notifyAll();
			
			
			for(int i=0;i<this.getNumeroVisitatoriPerGuida();++i)
				posso_uscire[lingua].signalAll();
			
		}finally {
			l.unlock();
		}
	}

}
