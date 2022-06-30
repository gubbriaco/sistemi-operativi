package laghetto.semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

import laghetto.Laghetto;

public class LaghettoSem extends Laghetto {
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore pesci;
	private Random random;
	private int nrPesci, stoAspettando = 0;
	private Semaphore pescare = new Semaphore(0), possoContinuare, nuovaPesca, nonAddetto, possoRipopolare = new Semaphore(0), ripopolare = new Semaphore(0);

	public LaghettoSem(int minPesci, int maxPesci, int nrPescatori, int nrAddetti) {
		super(minPesci, maxPesci, nrPescatori, nrAddetti);
		random = new Random();
		nrPesci = random.nextInt(minPesci, (maxPesci+1));
		System.out.println("NUMERO PESCI INIZIALE: " + nrPesci);
		pesci = new Semaphore(nrPesci-minPesci);
		nuovaPesca = new Semaphore(nrPescatori);
		nonAddetto = new Semaphore(nrPescatori);
		possoContinuare = new Semaphore(0);
	}

	@Override public void inizia(int t) throws InterruptedException {
		if(t == 0) {
			
			nuovaPesca.acquire();
			
			mutex.acquire();
			if(nrPesci == MIN_PESCI) {
				ripopolare.release();
				stoAspettando++;
				System.out.println("STANNO ASPETTANDO: " + stoAspettando + " persone");
				possoContinuare.acquire();
			}
			mutex.release();
			
			nonAddetto.acquire();
			pescare.release();
			
		}else if(t == 1) {
			
			ripopolare.acquire();
			possoRipopolare.release();
			
		}
	}

	@Override public void finisci(int t) throws InterruptedException {
		if(t == 0) {
			
			pescare.acquire();
			pesci.acquire();
			
			mutex.acquire();
			nrPesci--;
			System.out.println("NUMERO PESCI: " + nrPesci);
			mutex.release();
			
			nuovaPesca.release();
			
		}else if(t == 1) {
			
			possoRipopolare.acquire();
			
			pesci.release(10);
			
			mutex.acquire();
			nrPesci += 10;
			System.out.println("NUMERO PESCI: " + nrPesci);
			mutex.release();
			
			possoContinuare.release(stoAspettando);
			
			mutex.acquire();
			stoAspettando = 0;
			mutex.release();
			
			nonAddetto.release(nrPescatori);
			
		}
	}

	
	public static void main(String...strings) {
		int nrPescatori = 40, nrAddetti = 5;
		int minPesci = 50, maxPesci = 200;
		Laghetto l = new LaghettoSem(minPesci, maxPesci, nrPescatori, nrAddetti);
		l.test();
	}
	
}
