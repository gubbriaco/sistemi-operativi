package esercizio4.B.semaphore;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import esercizio4.Galleria;

public class GalleriaB_Sem extends Galleria {

	private Semaphore mutex = new Semaphore(1), termina_visita = new Semaphore(0);
	
	private Semaphore posso_entrare_in_galleria;
	private Semaphore[] guida, puoi_iniziare_visita, visita_terminata;
	
	private Semaphore[] sala;
	private int numero_sale, numero_visitatori_per_sala;
	private LinkedList<Thread> sala1, sala2, sala3;
	
	private LinkedList<Thread>[] in_attesa, in_visita;
	private LinkedList<Thread> in_galleria;
	

	public GalleriaB_Sem(int capienza, int numero_sale, int numero_visitatori_per_sala,
						int numero_guide, int numero_visitatori_per_guida) {
		super(capienza, numero_guide, numero_visitatori_per_guida);
		
		this.numero_sale = numero_sale;
		this.numero_visitatori_per_sala = numero_visitatori_per_sala;
		
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
		
		sala = new Semaphore[numero_sale];
		for(int i=0;i<numero_sale;++i)
			sala[i] = new Semaphore(numero_visitatori_per_sala);
		sala1 = new LinkedList<>();
		sala2 = new LinkedList<>();
		sala3 = new LinkedList<>();
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
		
		mutex.acquire();
		
		sala[0].acquire();
		sala1.addLast(visitatore);
		System.out.println(sala1.toString());
		
		sala[1].acquire();
		sala2.addLast(visitatore);
		System.out.println(sala2.toString());
		
		sala[2].acquire();
		sala3.addLast(visitatore);
		System.out.println(sala3.toString());
		
		mutex.release();
	
		
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
		sala[0].release(this.numero_visitatori_per_sala);
		TimeUnit.MINUTES.sleep(new Random().nextInt(30, 61));
		int rimanenti_sala1 = Integer.MAX_VALUE;
		while(rimanenti_sala1 > 0) {
			for(int i=0;i<this.numero_visitatori_per_sala;++i)
				sala1.removeFirst();
			System.out.println(sala1.toString());
			rimanenti_sala1 = sala1.size();
			sala[0].release(rimanenti_sala1);
		}
		sala1.clear(); System.out.println(sala1.toString());
		
		sala[1].release(this.numero_visitatori_per_sala);
		TimeUnit.MINUTES.sleep(new Random().nextInt(30, 61));
		int rimanenti_sala2 = Integer.MAX_VALUE;
		while(rimanenti_sala2 > 0) {
			for(int i=0;i<this.numero_visitatori_per_sala;++i)
				sala2.removeFirst();
			System.out.println(sala2.toString());
			rimanenti_sala2 = sala2.size();
			sala[1].release(rimanenti_sala2);
		}
		sala2.clear(); System.out.println(sala2.toString());	
		
		sala[2].release(this.numero_visitatori_per_sala);
		TimeUnit.MINUTES.sleep(new Random().nextInt(30, 61));
		int rimanenti_sala3 = Integer.MAX_VALUE;
		while(rimanenti_sala3 > 0) {
			for(int i=0;i<this.numero_visitatori_per_sala;++i)
				sala3.removeFirst();
			System.out.println(sala3.toString());
			rimanenti_sala3 = sala3.size();
			sala[2].release(rimanenti_sala3);
		}
		sala3.clear(); System.out.println(sala3.toString());
		
		
		termina_visita.acquire();
		
		mutex.acquire();
		
		visita_terminata[lingua].release(in_visita[lingua].size());
		
		guida[lingua].release(in_visita[lingua].size());
		
		in_visita[lingua].clear();
		System.out.println("IN VISITA: " + in_visita[lingua].toString());
		
		mutex.release();
	}
	
	
	public static void main(String...strings) {
		final int CAPIENZA_GALLERIA = 200, NUMERO_SALE = 3,
				  NUMERO_VISITATORI_PER_SALA = 60, NUMERO_GUIDE = 5, 
				  NUMERO_VISITATORI_PER_GUIDA = 20;
		Galleria galleria = new GalleriaB_Sem(CAPIENZA_GALLERIA, NUMERO_GUIDE, 
				NUMERO_SALE, NUMERO_VISITATORI_PER_SALA, NUMERO_VISITATORI_PER_GUIDA);
		final int NUMERO_VISITATORI = 300;
		galleria.test(NUMERO_VISITATORI);
	}
	

}
