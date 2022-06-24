package boccaccio.semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

import boccaccio.Boccaccio;

public class BoccaccioSemaphore extends Boccaccio {
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore[] caramelleSem = new Semaphore[this.numeroColoriDiversi];
	private Semaphore piangenti = new Semaphore(0);
	private Semaphore caramelleTerminate = new Semaphore(0);
	
	private int bambiniChePiangono = 0;

	public BoccaccioSemaphore(int dimensioneBoccaccio, int numeroColoriDiversi) {
		super(dimensioneBoccaccio, numeroColoriDiversi);
		
		for(int i=0;i<caramelle.length;++i)
			caramelleSem[i] = new Semaphore(this.numeroDiCaramellePerColore);
	}

	@Override public boolean prendi(int c) throws InterruptedException {
		Thread bambino = Thread.currentThread();
		boolean presa = true;
		
		mutex.acquire();
		if(this.caramelle[c] == 0) {
			System.out.println("Caramelle di colore " + c + " terminate!");
			bambiniChePiangono++;
			presa = false;
		}
		else {
			System.out.println(bambino.toString() + " sta prendendo una caramella di colore " + c);
			this.caramelle[c]--;
			caramelleSem[c].acquire();
			System.out.println(this.toString());
		}
		mutex.release();
		return presa;
	}

	@Override public void piangi() throws InterruptedException {
		
		Thread bambino = Thread.currentThread();
		System.out.println(bambino.toString() + " sta piangendo..");
		caramelleTerminate.release();
		piangenti.acquire();
		
	}

	@Override public void riempi() throws InterruptedException {
		caramelleTerminate.acquire(3);
		Thread addetto = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("Prima di riempire: " + this.toString());
		System.out.println(addetto.toString() + " sta riempiendo il boccaccio di caramelle..");

		riempiBoccaccio();
		
		for(int i=0;i<caramelleSem.length;++i) {
			caramelleSem[i] = new Semaphore(this.numeroDiCaramellePerColore);
		}
		
		System.out.println("Boccaccio riempito.");
		System.out.println("Dopo aver riempito: " + this.toString());
		
		bambiniChePiangono = 0;
		mutex.release();
		
		piangenti.release(bambiniChePiangono);
		
	}
	
	private void riempiBoccaccio() {
		numeroDiCaramellePerColore = dimensioneBoccaccio/numeroColoriDiversi;
		for(int i=0;i<numeroColoriDiversi;++i)
			caramelle[i] = numeroDiCaramellePerColore;
	}
	
	
	public static void main(String...strings) {
		int dimensioneBoccaccio = 100;
		int numeroColoriDiversi = 3;
		Boccaccio boccaccio = new BoccaccioSemaphore(dimensioneBoccaccio, numeroColoriDiversi);
		int numeroBambini = 100;
		boccaccio.test(numeroBambini);
		System.out.println("******* Le caramelle rimanenti sono: " + boccaccio.toString() + " *******");
	}

}
