package boccaccio.semaphore;

import java.util.concurrent.Semaphore;

import boccaccio.Boccaccio;

public class BoccaccioSemaphore extends Boccaccio {
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore piangenti = new Semaphore(0);
	private Semaphore[] caramelleSem;

	public BoccaccioSemaphore(int dimensioneBoccaccio, int numeroColoriDiversi) {
		super(dimensioneBoccaccio, numeroColoriDiversi);
		
		caramelleSem = new Semaphore[numeroColoriDiversi];
		for(int i=0;i<caramelleSem.length;++i)
			caramelleSem[i] = new Semaphore(this.numeroDiCaramellePerColore);
	}

	@Override public boolean prendi(int c) throws InterruptedException {
		caramelleSem[c].acquire();
		Thread bambino = Thread.currentThread();
		
		System.out.println(bambino.toString() + " sta cercando di prendere la caramella di colore " + c);
		mutex.acquire();
		caramelle[c]--;
		boolean presa = true;
		
		if(caramelle[c]==-1) {
			caramelle[c] = 0;
			System.out.println("Le caramelle di colore "  + c + " sono terminate..");
			presa = false;
		}
		
		System.out.println(this.toString());
		mutex.release();
		return presa;
	}

	@Override public void piangi() throws InterruptedException {
		Thread bambino = Thread.currentThread();
		
		System.out.println(bambino.toString() + " sta piangendo..");
		piangenti.release();
		
	}

	@Override public void riempi() throws InterruptedException {
		piangenti.acquire(3);
		
		Thread addetto = Thread.currentThread();
		System.out.println(addetto.toString() + " riempe il barattolo..");
		
		mutex.acquire();
		for(int i=0;i<numeroColoriDiversi;++i)
			caramelle[i] = numeroDiCaramellePerColore;
		for(int i=0;i<caramelleSem.length;++i)
			caramelleSem[i].release();
		System.out.println(this.toString());
		mutex.release();
		
		System.out.println(addetto.toString() + " ha finito di riempire il barattolo.");
	}
	
	
	
	public static void main(String...strings) {
		int dimensioneBoccaccio = 100;
		int numeroColoriDiversi = 3;
		Boccaccio boccaccio = new BoccaccioSemaphore(dimensioneBoccaccio, numeroColoriDiversi);
		int numeroBambini = 150;
		boccaccio.test(numeroBambini);
		System.out.println("******* Le caramelle rimanenti sono: " + boccaccio.toString() + " *******");
	}

	
}
