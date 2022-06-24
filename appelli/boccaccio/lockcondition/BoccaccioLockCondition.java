package boccaccio.lockcondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import boccaccio.Boccaccio;

public class BoccaccioLockCondition extends Boccaccio {
	
	private Lock l = new ReentrantLock();
	private Condition[] possoPrendere = new Condition[this.dimensioneBoccaccio];
	private Condition riempito = l.newCondition();
	
	private int bambiniChePiangono = 0;

	public BoccaccioLockCondition(int dimensioneBoccaccio, int numeroColoriDiversi) {
		super(dimensioneBoccaccio, numeroColoriDiversi);
		
		for(int i=0;i<this.dimensioneBoccaccio;++i)
			possoPrendere[i] = l.newCondition();
	}

	@Override public boolean prendi(int c) throws InterruptedException {
		Thread bambino = Thread.currentThread();
		boolean presa = true;
		l.lock();
		try {
			
			while(this.caramelle[c] == 0)
			
			if(this.caramelle[c]==0) {
				presa = false;
				bambiniChePiangono++;
				System.out.println("Caramelle di colore " + c + " terminate!");
			}
			

			System.out.println(bambino.toString() + " sta prendendo una caramella di colore " + c);
			this.caramelle[c]--;
			System.out.println(this.toString());
			
		}finally {
			l.unlock();
			return presa;
		}
	}

	@Override public void piangi() throws InterruptedException {
		l.lock();
		Thread bambino = Thread.currentThread();
		try {
			
			System.out.println(bambino.toString() + " sta piangendo..");
			riempito.signal();
			
		}finally {
			l.unlock();
		}
	}

	@Override public void riempi() throws InterruptedException {
		l.lock();
		Thread addetto = Thread.currentThread();
		try {
			
			while (bambiniChePiangono<3)
                riempito.await();
			
			System.out.println("Prima di riempire: " + this.toString());
			System.out.println(addetto.toString() + " sta riempiendo il boccaccio di caramelle..");
			
			riempiBoccaccio();
			riempito.signalAll();
			
			System.out.println("Boccaccio riempito.");
			System.out.println("Dopo aver riempito: " + this.toString());
			
		}finally {
			l.unlock();
		}
	}
	
	private void riempiBoccaccio() {
		numeroDiCaramellePerColore = dimensioneBoccaccio/numeroColoriDiversi;
		for(int i=0;i<numeroColoriDiversi;++i)
			caramelle[i] = numeroDiCaramellePerColore;
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
