package centro_storico.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import centro_storico.TourFirenze;

public class TourFirenzeSemaphore extends TourFirenze {

	private Semaphore mutex = new Semaphore(1);
	
	private Semaphore gruppo;
	private Semaphore inizioVisita;
	private Semaphore fineVisita;
	private Semaphore guidaPuoOrganizzareNuovaVisita = new Semaphore(0);
	private Semaphore nuovaVisita = new Semaphore(1);
	
	private LinkedList<Thread> gruppoTuristi;
	
	public TourFirenzeSemaphore(int nr_turisti_per_gruppo) {
		super(nr_turisti_per_gruppo);
		
		gruppo = new Semaphore(0);
		inizioVisita = new Semaphore(0);
		fineVisita = new Semaphore(0);
		
		gruppoTuristi = new LinkedList<>();
	}

	@Override public void attendiFormazioneGruppo() throws InterruptedException {
		Thread guida = Thread.currentThread();
		System.out.println("La " + guida.toString() + " attende la formazione del gruppo di turisti..");
		
		gruppo.acquire(this.nr_turisti_per_gruppo);
		nuovaVisita.acquire();
	}

	@Override public void visitaInizia() throws InterruptedException {
		inizioVisita.release(this.nr_turisti_per_gruppo);
		Thread guida = Thread.currentThread();
		System.out.println("La " + guida.toString() + " inizia la visita in citta'.");
		
		mutex.acquire();
		System.out.println("GRUPPO: " + gruppoTuristi.toString());
		mutex.release();
		
	}

	@Override public void visitaFine() throws InterruptedException {
		fineVisita.release(this.nr_turisti_per_gruppo);
		
		Thread guida = Thread.currentThread();
		System.out.println("La " + guida + " ha terminato la visita in citta'.");
		
		guidaPuoOrganizzareNuovaVisita.acquire(this.nr_turisti_per_gruppo);
		System.out.println("La " + guida + " organizza una nuova visita in citta'.");
		nuovaVisita.release();
	}

	@Override public void turistaInizia() throws InterruptedException {
		gruppo.release();
		
		Thread turista = Thread.currentThread();
		System.out.println("Il " + turista + " cerca di aggiungersi al gruppo di turisti per la nuova visita..");
		inizioVisita.acquire();
		
		mutex.acquire();
		System.out.println("Il " + turista.toString() + " inizia la visita della citta.");
		gruppoTuristi.add(turista);
		System.out.println("GRUPPO TURISTI: " + gruppoTuristi.toString());
		mutex.release();
	}

	@Override public void turistaFine() throws InterruptedException {
		fineVisita.acquire();
		
		Thread turista = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("Il " + turista.toString() + " ha terminato la visita in citta'.");
		gruppoTuristi.remove(turista);
		this.nr_turisti--;
		System.out.println("GRUPPO TURISTI: " + gruppoTuristi.toString());
		mutex.release();
		
		guidaPuoOrganizzareNuovaVisita.release();
	}
	
	
	public static void main(String...strings) {
		int nr_turisti_per_gruppo = 20;
		TourFirenze tf = new TourFirenzeSemaphore(nr_turisti_per_gruppo);
		int nr_turisti = 100;
		tf.test(nr_turisti);
	}

}
