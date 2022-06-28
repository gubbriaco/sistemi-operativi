package museo.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import museo.Museo;

public class MuseoSemaphoreVersioneB extends Museo {
	
	private Semaphore mutex = new Semaphore(1);
	
	private Semaphore possoEntrareInArcheologica, possoEntrareInDama;
	private Semaphore possoPassareAllaProssimaSala = new Semaphore(5);
	
	private LinkedList<Thread> inArcheologica, inDama, gruppoVersoDama;
	
	private static int nrPersoneGruppoDama = 5;

	public MuseoSemaphoreVersioneB(int nrPersoneArcheologica, int nrPersoneDama) {
		super(nrPersoneArcheologica, nrPersoneDama);
		
		possoEntrareInArcheologica = new Semaphore(nrPersoneArcheologica);
		possoEntrareInDama = new Semaphore(0);
		
		inArcheologica = new LinkedList<>();
		inDama = new LinkedList<>();
		gruppoVersoDama = new LinkedList<>();
	}

	@Override public void visitaSA() throws InterruptedException {
		possoEntrareInArcheologica.acquire();
		Thread visitatore = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("Il" + visitatore.toString() + " e' entrato nella sala archeologica.");
		inArcheologica.add(visitatore);
		System.out.println("SALA ARCHEOLOGICA: " + inArcheologica.toString());
		System.out.println("Persone nella SALA ARCHEOLOGICA: " + inArcheologica.size());
		mutex.release();
	}

	@Override public void terminaVisitaSA() throws InterruptedException {
		Thread visitatore = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("Il" + visitatore.toString() + " e' uscito dalla sala archeologica.");
		inArcheologica.remove(visitatore);
		System.out.println("SALA ARCHEOLOGICA: " + inArcheologica.toString());
		System.out.println("Persone nella SALA ARCHEOLOGICA: " + inArcheologica.size());
		possoEntrareInArcheologica.release();
		
		possoPassareAllaProssimaSala.acquire();
		if(gruppoVersoDama.size() <= nrPersoneGruppoDama) {
			gruppoVersoDama.add(visitatore);
			System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.toString());
			System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.size());
			
		}
		
		if(gruppoVersoDama.size() == nrPersoneGruppoDama)
			possoEntrareInDama.release(nrPersoneGruppoDama);
		
		mutex.release();
		
	}

	@Override public void visitaSD() throws InterruptedException {
		possoEntrareInDama.acquire();
		Thread visitatore = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("Il " + visitatore.toString() + " e' entrato nella sala della dama.");
		inDama.add(visitatore);
		System.out.println("SALA DELLA DAMA: " + inDama.toString());
		System.out.println("Persone nella SALA DELLA DAMA: " + inDama.size());
		mutex.release();
	}

	@Override public void terminaVisitaSD() throws InterruptedException {
		Thread visitatore = Thread.currentThread();
	
		mutex.acquire();
		System.out.println("Il " + visitatore.toString() + " e' uscito dalla sala della dama.");
		inDama.remove(visitatore);
		gruppoVersoDama.remove(visitatore);
		System.out.println("SALA DELLA DAMA: " + inDama.toString());
		System.out.println("Persone nella SALA DELLA DAMA: " + inDama.size());
		System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.toString());
		System.out.println("GRUPPO PER LA SALA DELLA DAMA: " + gruppoVersoDama.size());
		mutex.release();
		
		possoPassareAllaProssimaSala.release(nrPersoneGruppoDama);
	}
	
	
	public static void main(String...strings) {
		int nrPersoneFilaArcheologica = 40;
		int nrPersoneFilaDama = 5;
		Museo museo = new MuseoSemaphoreVersioneB(nrPersoneFilaArcheologica, nrPersoneFilaDama);
		int numeroVisitatori = 50;
		museo.test(numeroVisitatori);
	}

}
