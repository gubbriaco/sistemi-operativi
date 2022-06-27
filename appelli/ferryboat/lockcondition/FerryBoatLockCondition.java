package ferryboat.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ferryboat.FerryBoat;

public class FerryBoatLockCondition extends FerryBoat {

	private Lock l = new ReentrantLock();
	private Condition possoSalire = l.newCondition();
	private Condition possoParcheggiare = l.newCondition();
	private Condition possoScendere = l.newCondition();
	
	private Condition parcheggiata = l.newCondition();
	private Condition traghettata = l.newCondition();
	
	private LinkedList<Thread> inFilaPerSalire;
	private LinkedList<Thread> sulTraghetto;
	private LinkedList<Thread> parcheggiate;
	
	public FerryBoatLockCondition(int capienza) {
		super(capienza);
		
		inFilaPerSalire = new LinkedList<>();
		sulTraghetto = new LinkedList<>();
		parcheggiate = new LinkedList<>();
	}

	@Override public void sali() throws InterruptedException {
		l.lock();
		Thread auto = Thread.currentThread();
		try {
			System.out.println("L'" + auto.toString() + " si mette in fila per salire.");
			inFilaPerSalire.addLast(auto); /** auto in fila */
			System.out.println("IN FILA: " + inFilaPerSalire.toString());
			
			while(!mioTurnoPerSalire(auto))
				possoSalire.await();
			
			System.out.println("L'" + auto.toString() + " sta salendo sul traghetto..");
			System.out.println("L'" + auto.toString() + " e' salita sul traghetto.");
			sulTraghetto.addLast(auto); /** auto sul traghetto */
			System.out.println("SUL TRAGHETTO: " + sulTraghetto.toString());

		}finally {
			l.unlock();
		}
	}
	
	private boolean mioTurnoPerSalire(Thread auto) {
		return inFilaPerSalire.getFirst().equals(auto);
	}

	
	@Override public void parcheggiaEScendi() throws InterruptedException {
		l.lock();
		Thread auto = Thread.currentThread();
		try {
			while(!possoParcheggiarmi(auto))
				possoParcheggiare.await();
			
			parcheggiate.addFirst(auto); /** auto parcheggiata */ 
			System.out.println("L'" + auto.toString() + " si sta parcheggiando sul traghetto..");
			System.out.println("L'" + auto.toString() + " si e' parcheggiata sul traghetto.");
			System.out.println("PARCHEGGIATE: " + parcheggiate.toString());
			parcheggiata.signal(); /** mi sono parcheggiata */

			/** significa che l'ultima auto in fila e' salita e quindi il traghetto puo' partire */
			if(inFilaPerSalire.size()==0)
				traghettata.signal();
			
			while(!mioTurnoPerScendere(auto))
				possoScendere.await();
			System.out.println("L'" + auto.toString() + " sta scendendo dal traghetto..");
			System.out.println("L'" + auto.toString() + " e' scesa dal traghetto.");
			/** l'auto puo'scendere */
			
			parcheggiate.removeLast();
			System.out.println("PARCHEGGIATE: " + parcheggiate.toString());
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoParcheggiarmi(Thread auto) {
		return sulTraghetto.getLast().equals(auto);
	}
	
	private boolean mioTurnoPerScendere(Thread auto) {
		return sulTraghetto.getFirst().equals(auto);
	}

	@Override public void imbarca() throws InterruptedException {
		l.lock();
		Thread addetto = Thread.currentThread();
		try {
			
			possoSalire.signal();
			Thread auto = inFilaPerSalire.removeFirst();
			System.out.println("IN FILA: " + inFilaPerSalire.toString());
			System.out.println("L'" + addetto.toString() + " sta facendo parcheggiare l'" + auto.toString() + "..");
			
			possoParcheggiare.signal();
			
			while(!parcheggiata(auto))
				parcheggiata.await();

			
		}finally {
			l.unlock();
		}
	}
	
	private boolean parcheggiata(Thread auto) {
		return parcheggiate.getFirst().equals(auto);
	}

	@Override public void terminaTraghettata() throws InterruptedException {
		l.lock();
		Thread addetto = Thread.currentThread();
		try {

			Thread auto = sulTraghetto.removeLast();
			System.out.println("SUL TRAGHETTO: " + sulTraghetto.toString());
			
			while(!possoTerminareTraghettata())
				traghettata.await();
			
			System.out.println("L'" + addetto.toString() + " fa scendere l'" + auto.toString() + " dal traghetto.");
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoTerminareTraghettata() {
		return sulTraghetto.size() == 0;
	}
	
	
	public static void main(String...strings) {
		int capienza = 50;
		FerryBoat ferryBoat = new FerryBoatLockCondition(capienza);
		int numeroAuto = 50;
		ferryBoat.test(numeroAuto);
		
	}

}
