package trenino.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import trenino.Trenino;

public class TreninoSemaphore extends Trenino {
	
	private Semaphore mutex = new Semaphore(1);
	
	private Semaphore[] cabineSem;
	
	private Semaphore possoSalire = new Semaphore(0);
	private Semaphore possoScendere = new Semaphore(0);
	
	private Semaphore prossimaCabina = new Semaphore(0);
	
	private Semaphore possonoSalire = new Semaphore(0);
	private Semaphore possonoScendere = new Semaphore(1);
	
	private LinkedList<Thread> inAttesaDiSalire;
	private LinkedList<Thread> inAttesaDiScendere;
	
	private int numeroCabina;

	public TreninoSemaphore(int dimensioneCabina, int numeroCabine) {
		super(dimensioneCabina, numeroCabine);
		
		numeroCabina = 0;
		
		cabineSem = new Semaphore[numeroCabine];
		for(int i=0;i<cabineSem.length;++i)
			cabineSem[i] = new Semaphore(0);
		
		inAttesaDiSalire = new LinkedList<>();
		inAttesaDiScendere = new LinkedList<>();
	}

	@Override public void turistaSali() throws InterruptedException {
		Thread turista = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("Il" + turista.toString() + " intende salire sulla cabina " + numeroCabina);
		inAttesaDiSalire.add(turista);
		System.out.println("IN ATTESA DI SALIRE: " + inAttesaDiSalire.toString());
		this.cabine[numeroCabina]++;
		mutex.release();
		
		if(cabine[numeroCabina]==this.dimensioneCabina)
			prossimaCabina.release();

		possoSalire.acquire();
	}
	

	@Override public void turistaScendi() throws InterruptedException {
		Thread turista = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("Il" + turista.toString() + " intende scendere dalla cabina " + numeroCabina);
		inAttesaDiScendere.add(turista);
		System.out.println("IN ATTESA DI SCENDERE: " + inAttesaDiScendere.toString());
		this.cabine[numeroCabina]--;
		mutex.release();
		
		if(cabine[numeroCabina]==0)
			possoSalire.release();
		
		possoScendere.acquire();
		
	}
	

	@Override public void impiegatoFaiSalire() throws InterruptedException {
		possonoSalire.acquire();
		Thread impiegato = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("L'" + impiegato.toString() + " fa salire i turisti sulla cabina " + numeroCabina);
		
		mutex.release();
		possoSalire.release(this.dimensioneCabina);
		prossimaCabina.release();
	}

	@Override public void impiegatoFaiScendere() throws InterruptedException {
		possonoScendere.acquire();
		Thread impiegato = Thread.currentThread();
		
		mutex.acquire();
		System.out.println("L'" + impiegato.toString() + " fa scendere i turisti dalla cabina " + numeroCabina);
		inAttesaDiScendere.clear();
		int permessiCabina = this.cabine[numeroCabina];
		this.cabine[0] = 0;
		System.out.println("CABINA " + numeroCabina + ": " + this.cabine[numeroCabina] + " turisti");
		mutex.release();

		possoScendere.release(permessiCabina);
		possonoSalire.release();
		
	}


	@Override public void impiegatoMuovi() throws InterruptedException {
		
		prossimaCabina.acquire();
		
		mutex.acquire();
		
		System.out.println("CABINA " + numeroCabina + ": " + this.cabine[numeroCabina] + " turisti");
		int tmpCabina = numeroCabina;
		numeroCabina++;
		System.out.println("CABINA: " + tmpCabina + " -> CABINA: " + numeroCabina);
		
		mutex.release();
		
		possonoScendere.release();
	}
	
	
	public static void main(String...strings) {
		int dimensioneCabina = 10;
		int numeroCabine = 2;
		Trenino trenino = new TreninoSemaphore(dimensioneCabina, numeroCabine);
		int numeroTuristi = 18;
		trenino.test(numeroTuristi);
	}

}
