package boccaccio;

import java.util.Arrays;
import java.util.Random;

public abstract class Boccaccio {
	
	protected int numeroColoriDiversi;
	protected int[] caramelle;
	protected int dimensioneBoccaccio;
	protected int numeroDiCaramellePerColore;
	
	public Boccaccio(int dimensioneBoccaccio, int numeroColoriDiversi) {
		this.numeroColoriDiversi = numeroColoriDiversi;
		
		this.dimensioneBoccaccio = dimensioneBoccaccio;
		caramelle = new int[numeroColoriDiversi];
		
		numeroDiCaramellePerColore = dimensioneBoccaccio/numeroColoriDiversi;
		/** assegno per un numeroDiCaramellePerColore il colore corrispondente c=i+1 */
		for(int i=0;i<numeroColoriDiversi;++i)
			caramelle[i] = numeroDiCaramellePerColore;
		
		System.out.println(this.toString());
		
	}
	
	/**
	 * Permette di prendere al bambino una caramella di colore c. Restituisce true 
	 * se il bambino &egrave riuscito a prendere una caramella, false altrimenti.
	 * @param c Caramella di colore c
	 * @return caramella trovata/restituita
	 * @throws InterruptedException
	 */
	public abstract boolean prendi(int c) throws InterruptedException;
	
	/**
	 * Permette al bambino di piangere nel caso in cui le caramelle del colore c
	 * che ha richiesto sono terminate.
	 * @throws InterruptedException
	 */
	public abstract void piangi() throws InterruptedException;
	
	/**
	 * Permette all'addetto di riempire il boccaccio.
	 * @throws InterruptedException
	 */
	public abstract void riempi() throws InterruptedException;
	
	
	@Override public String toString() {
		return Arrays.toString(caramelle);
	}
	
	
	public void test(int numeroBambini) {
		
		Random random;
		
		Thread[] bambini = new Bambino[numeroBambini];
		for(int i=0;i<numeroBambini;++i) {
			random = new Random();
			int coloreCaramella = random.nextInt(0, numeroColoriDiversi);
			bambini[i] = new Bambino(i+1 ,this, coloreCaramella);
			System.out.println(bambini[i].toString() + " ha deciso di prendere una caramella di colore " + coloreCaramella);
			bambini[i].start();
		}
		
		Thread addetto = new Addetto(this);
		addetto.start();
		
	}
	
}
