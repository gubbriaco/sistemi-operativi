package ferryboat.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import ferryboat.FerryBoat;

public class FerryBoatSemaphore extends FerryBoat {
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore possoSalire = new Semaphore(0);
	private Semaphore possoParcheggiare = new Semaphore(0);
	private Semaphore possoScendere = new Semaphore(0);
	
	private LinkedList<Thread> inFilaPerSalire;
	private LinkedList<Thread> sulTraghetto;
	private LinkedList<Thread> parcheggiate;

	public FerryBoatSemaphore(int capienza) {
		super(capienza);
		
		inFilaPerSalire = new LinkedList<>();
		sulTraghetto = new LinkedList<>();
		parcheggiate = new LinkedList<>();
	}

	@Override public void sali() throws InterruptedException {
		Thread auto = Thread.currentThread();
		mutex.acquire();
		System.out.println("L'" + auto.toString() + " si sta mettendo in fila per salire..");
		System.out.println("L'" + auto.toString() + " e' in fila per salire.");
		inFilaPerSalire.add(auto);
		System.out.println("IN FILA: " + inFilaPerSalire.toString());
		mutex.release();
		
		possoSalire.acquire();
		
		mutex.acquire();
		System.out.println("L'" + auto.toString() + " sta salendo sulla " + this.toString() + "..");
		System.out.println("L'" + auto.toString() + " e' salita sulla " + this.toString());
		sulTraghetto.add(auto);
		System.out.println("SUL TRAGHETTO: " + sulTraghetto.toString());
		mutex.release();
	}

	@Override public void parcheggiaEScendi() throws InterruptedException {
		possoParcheggiare.acquire();
		Thread auto = Thread.currentThread();
		mutex.acquire();
		System.out.println("L'" + auto.toString() + " si sta parcheggiando sulla " + this.toString() + "..");
		System.out.println("L'" + auto.toString() + " si e' parcheggiata sulla " + this.toString());
		parcheggiate.add(auto);
		System.out.println("PARCHEGGIATE: " + parcheggiate.toString());
		mutex.release();
		
		possoScendere.acquire();
		mutex.acquire();
		System.out.println("L'" + auto.toString() + " sta scendendo dalla " + this.toString() + "..");
		System.out.println("L'" + auto.toString() + " e' scesa dalla " + this.toString());
		mutex.release();
	}

	@Override public void imbarca() throws InterruptedException {
		possoSalire.release();
		Thread addetto = Thread.currentThread();
		mutex.acquire();
		Thread auto = inFilaPerSalire.removeFirst();
		System.out.println("L'" + addetto.toString() + " sta facendo salire l'" + auto.toString() + " sulla " + this.toString() + "..");
		System.out.println("L'" + addetto.toString() + " ha fatto salire l'" + auto.toString() + " sulla " + this.toString());
		System.out.println("IN FILA: " + inFilaPerSalire.toString());
		mutex.release();
		
		possoParcheggiare.release();
		mutex.acquire();
		System.out.println("L'" + addetto.toString() + " sta facendo parcheggiare l'" + auto.toString() + " sulla " + this.toString() + "..");
		System.out.println("L'" + addetto.toString() + " ha fatto parcheggiare l'" + auto.toString() + " sulla " + this.toString());
		mutex.release();
	}

	@Override public void terminaTraghettata() throws InterruptedException {
		Thread addetto = Thread.currentThread();
		mutex.acquire();
		if(inFilaPerSalire.size() == 0) {
			System.out.println("******************************************");
			System.out.println("Il " + this.toString() + " e' arrivatto a destinazione!");
			possoScendere.release(this.capienza);
			System.out.println("L'" + addetto.toString() + " sta facendo scendere le auto dal " + this.toString());
			
			for(int i=0;i<this.capienza;++i) {
				sulTraghetto.removeLast();
				Thread auto = parcheggiate.removeLast();
				System.out.println("L'" + addetto.toString() + " sta facendo scendere l'" + auto.toString() + " dal " + this.toString() + "..");
				System.out.println("L'" + addetto.toString() + " ha fatto scendere l'" + auto.toString() + " dal " + this.toString());
				System.out.println("SUL TRAGHETTO: " + sulTraghetto.toString());
				System.out.println("PARCHEGGIATE: " + parcheggiate.toString());
			}
		}
		mutex.release();
	}
	
	
	public static void main(String...strings) {
		int capienza = 50;
		FerryBoat ferryBoat = new FerryBoatSemaphore(capienza);
		int numeroAuto = 50;
		ferryBoat.test(numeroAuto);
		
	}

}
