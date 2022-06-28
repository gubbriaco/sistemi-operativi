package museo.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import museo.Museo;

public class MuseoSemaphoreVersioneA extends Museo {
	
	private Semaphore mutex = new Semaphore(1);
	
	private Semaphore possoEntrareInArcheologica, possoEntrareInDama;
	private Semaphore possoPassareAllaProssimaSala = new Semaphore(0);
	
	private LinkedList<Thread> inArcheologica, inDama;

	public MuseoSemaphoreVersioneA(int nrPersoneArcheologica, int nrPersoneDama) {
		super(nrPersoneArcheologica, nrPersoneDama);
		
		possoEntrareInArcheologica = new Semaphore(nrPersoneArcheologica);
		possoEntrareInDama = new Semaphore(nrPersoneDama);
		
		inArcheologica = new LinkedList<>();
		inDama = new LinkedList<>();
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
		mutex.release();
		
		possoEntrareInArcheologica.release();
		possoPassareAllaProssimaSala.release();
	}

	@Override public void visitaSD() throws InterruptedException {
		possoPassareAllaProssimaSala.acquire();
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
		System.out.println("SALA DELLA DAMA: " + inDama.toString());
		System.out.println("Persone nella SALA DELLA DAMA: " + inDama.size());
		mutex.release();
		
		possoEntrareInDama.release();
	}
	
	
	public static void main(String...strings) {
		int nrPersoneFilaArcheologica = 40;
		int nrPersoneFilaDama = 5;
		Museo museo = new MuseoSemaphoreVersioneA(nrPersoneFilaArcheologica, nrPersoneFilaDama);
		int numeroVisitatori = 50;
		museo.test(numeroVisitatori);
	}

}
