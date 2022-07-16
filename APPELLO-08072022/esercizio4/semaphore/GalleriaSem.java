package esercizio4.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import esercizio4.Galleria;

public class GalleriaSem extends Galleria {
	
	private Semaphore mutex = new Semaphore(1), termina_visita = new Semaphore(0);
	
	private Semaphore posso_entrare_in_galleria;
	
	private Semaphore[] guida, puoi_iniziare_visita, visita_terminata;
	
	private LinkedList<Thread>[] in_attesa, in_visita;
	private LinkedList<Thread> in_galleria;

	@SuppressWarnings("unchecked")
	public GalleriaSem(int capienza, int numero_guide, int numero_visitatori_per_guida) {
		super(capienza, numero_guide, numero_visitatori_per_guida);
		
		guida = new Semaphore[numero_guide];
		puoi_iniziare_visita = new Semaphore[numero_guide];
		visita_terminata = new Semaphore[numero_guide];
		for(int i=0;i<numero_guide;++i) {
			guida[i] = new Semaphore(numero_visitatori_per_guida);
			puoi_iniziare_visita[i] = new Semaphore(0);
			visita_terminata[i] = new Semaphore(0);
		}
		
		in_attesa = new LinkedList[numero_guide];
		in_visita = new LinkedList[numero_guide];
		for(int i=0;i<numero_guide;++i) {
			in_attesa[i] = new LinkedList<Thread>();
			in_visita[i] = new LinkedList<Thread>();
		}
		
		in_galleria = new LinkedList<>();
		posso_entrare_in_galleria = new Semaphore(capienza);
		
	}

	@Override public void iniziaVisita(int lingua) throws InterruptedException {
		Thread visitatore = Thread.currentThread();
		
		mutex.acquire();
		in_attesa[lingua].addLast(visitatore);
		System.out.println("IN ATTESA: " + in_attesa[lingua].toString());
		mutex.release();
		
		/** gestisce la capienza nella galleria */
		posso_entrare_in_galleria.acquire();
		
		/** prende il posto con la corrispondente guida */
		guida[lingua].acquire();
		
		mutex.acquire();
		in_attesa[lingua].remove(visitatore);
		System.out.println("IN ATTESA: " + in_attesa[lingua].toString());
		in_visita[lingua].addLast(visitatore);
		System.out.println("IN VISITA: " + in_visita[lingua].toString());
		in_galleria.addLast(visitatore);
		System.out.println("IN GALLERIA: " + in_galleria.toString());
		mutex.release();
		
		puoi_iniziare_visita[lingua].acquire();
	}

	@Override public void esci(int lingua) throws InterruptedException {
		Thread visitatore = Thread.currentThread();
		visita_terminata[lingua].acquire();
		
		mutex.acquire();
		in_galleria.remove(visitatore);
		System.out.println("IN GALLERIA: " + in_galleria.toString());
		mutex.release();
		
		/** esco dalla galleria */
		posso_entrare_in_galleria.release();
	}

	@Override public void attendiVisitatori(int lingua) throws InterruptedException {
		puoi_iniziare_visita[lingua].release(this.getNumeroVisitatoriPerGuida());
		
		termina_visita.release();
	}

	@Override public void terminaVisita(int lingua) throws InterruptedException {
		termina_visita.acquire();
		
		
		mutex.acquire();
		
		visita_terminata[lingua].release(in_visita[lingua].size());
		
		guida[lingua].release(in_visita[lingua].size());
		
		in_visita[lingua].clear();
		System.out.println("IN VISITA: " + in_visita[lingua].toString());
		
		mutex.release();
		
	}
	
	
	public static void main(String...strings) {
		final int CAPIENZA_GALLERIA = 200, NUMERO_GUIDE = 5, NUMERO_VISITATORI_PER_GUIDA = 20;
		Galleria galleria = new GalleriaSem(CAPIENZA_GALLERIA, NUMERO_GUIDE, NUMERO_VISITATORI_PER_GUIDA);
		final int NUMERO_VISITATORI = 300;
		galleria.test(NUMERO_VISITATORI);
	}
	

}
