package laghetto.lockcondition;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import laghetto.Laghetto;

public class LaghettoLC extends Laghetto {

	private Lock l = new ReentrantLock();
	private Condition pescare = l.newCondition(), ripopolare = l.newCondition();
	private LinkedList<Thread> pescatori = new LinkedList<>(), addetti = new LinkedList<>(), inAttesa = new LinkedList<>();
	private int nrPesci;
	private Random random;
	
	public LaghettoLC(int minPesci, int maxPesci, int nrPescatori, int nrAddetti) {
		super(minPesci, maxPesci, nrPescatori, nrAddetti);
		random = new Random();
		nrPesci = random.nextInt(minPesci, maxPesci+1);
		System.out.println("NUMERO PESCI INIZIALE: " + nrPesci);
	}

	@Override public void inizia(int t) throws InterruptedException {
		l.lock();
		try {
			
			Thread current = Thread.currentThread();
		
			if(t == 0) {
				
				pescatori.add(current);
				
				if(nrPesci == MIN_PESCI) {
					ripopolare.signalAll();
					inAttesa.add(current);
					System.out.println("IN ATTESA: " + inAttesa);
					
				}
				
				while(nonPossoPescare())
					pescare.await();
				
				
			}else if(t == 1) {
				
				addetti.add(current);
				
				while(nonPossoRipopolare())
					ripopolare.await();
			}
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean nonPossoPescare() {
		return nrPesci == MIN_PESCI;
	}
	
	private boolean nonPossoRipopolare() {
		return nrPesci > MIN_PESCI;
	}

	@Override public void finisci(int t) throws InterruptedException {
		l.lock();
		try {
			
			Thread current = Thread.currentThread();
		
			if(t == 0) {
				
				while(nonPossoPescare())
					pescare.await();
				
				nrPesci--;
				System.out.println("NUMERO PESCI: " + nrPesci);
				
				pescatori.remove(current);
				
			}else if(t == 1) {
				
				while(nonPossoRipopolare())
					ripopolare.await();
				
				nrPesci+=10;
				System.out.println("NUMERO PESCI: " + nrPesci);
				inAttesa.clear();
				System.out.println("IN ATTESA: " + inAttesa);
				pescare.signalAll();
				
				addetti.remove(current);
			}
			
		}finally {
			l.unlock();
		}
	}
	
	
	public static void main(String...strings) {
		int nrPescatori = 40, nrAddetti = 5;
		int minPesci = 50, maxPesci = 200;
		Laghetto l = new LaghettoLC(minPesci, maxPesci, nrPescatori, nrAddetti);
		l.test();
	}

}
