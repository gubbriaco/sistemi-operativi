package boccaccio.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import boccaccio.Boccaccio;

public class BoccaccioLockCondition extends Boccaccio {
	
	private Lock l = new ReentrantLock();
	private Condition[] caramelleCond;
	@SuppressWarnings("unused")
	private Condition riempi = l.newCondition();
	
	private static int i = 0, indice = -1;
	
	
	private LinkedList<Thread> bambini, piangenti; 

	public BoccaccioLockCondition(int dimensioneBoccaccio, int numeroColoriDiversi) {
		super(dimensioneBoccaccio, numeroColoriDiversi);
		
		caramelleCond = new Condition[dimensioneBoccaccio];
		for(int i=0;i<caramelleCond.length;++i)
			caramelleCond[i] = l.newCondition();
		
		bambini = new LinkedList<>();
		piangenti = new LinkedList<>();
	}

	@SuppressWarnings("finally")
	@Override public boolean prendi(int c) throws InterruptedException {
		l.lock();
		boolean presa = true;
		try {
			
			if(c==0) {
				indice = (i+1)%33;
				i++;
			}else if(c==1) {
				indice = (i+33)%66;
				i++;
			}else if(c==2) {
				indice = (i+66)%99;
				i++;
			}
			
			if(caramelle[indice] == 0)
				presa = false;
			else
				caramelle[indice]--;
			
			Thread bambino = Thread.currentThread();
			bambini.addLast(bambino);
			
		}finally {
			l.unlock();
			return presa;
		}
	}

	@Override public void piangi() throws InterruptedException {
		l.lock();
		
		try {
			
			Thread bambino = Thread.currentThread();
			piangenti.addLast(bambino);
			
			while(caramelle[indice]==0 || !bambini.getFirst().equals(bambino))
				caramelleCond[indice].await();
			
		}finally {
			l.unlock();
		}
	}

	@Override public void riempi() throws InterruptedException {
		l.lock();
		
		try {
			
			caramelleCond[indice].signalAll();
			
		}finally {
			l.unlock();
		}
	}
	
	
	public static void main(String...strings) {
		int dimensioneBoccaccio = 100;
		int numeroColoriDiversi = 3;
		Boccaccio boccaccio = new BoccaccioLockCondition(dimensioneBoccaccio, numeroColoriDiversi);
		int numeroBambini = 100;
		boccaccio.test(numeroBambini);
		System.out.println("******* Le caramelle rimanenti sono: " + boccaccio.toString() + " *******");
	}
	
	
}
