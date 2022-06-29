package centro_storico.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import centro_storico.TourFirenze;

public class TourFirenzeLockCondition extends TourFirenze {
	
	private Lock l = new ReentrantLock();
	
	private Condition possoIniziareNuovoTour = l.newCondition();
	
	private Condition gruppoFormato = l.newCondition();
	private Condition terminaTour = l.newCondition();
	
	private Condition possoUnirmiAlGruppo = l.newCondition();

	private LinkedList<Thread> gruppo, turisti;
	
	private boolean possonoTerminareTour;
	
	public TourFirenzeLockCondition(int nr_turisti_per_gruppo) {
		super(nr_turisti_per_gruppo);
		
		gruppo = new LinkedList<>();
		turisti = new LinkedList<>();
		
		possonoTerminareTour = false;
	}

	@Override public void attendiFormazioneGruppo() throws InterruptedException {
		l.lock();
		Thread guida = Thread.currentThread();
		try {
			
			System.out.println("La " + guida.toString() + " organizza un nuovo tour.");
			while(!possoIniziareNuovoTour())
				possoIniziareNuovoTour.await();
			
			possoUnirmiAlGruppo.signalAll();
			
			System.out.println("La " + guida.toString() + " sta attendendo che si formi il gruppo..");
			while(!gruppoFormato())
				gruppoFormato.await();
			
			System.out.println("Il gruppo si e' formato.");
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoIniziareNuovoTour() {
		return turisti.size() == 0;
	}
	
	private boolean gruppoFormato() {
		return gruppo.size() == this.nr_turisti_per_gruppo;
	}

	@Override public void visitaInizia() throws InterruptedException {
		l.lock();
		Thread guida = Thread.currentThread();
		try {
			
			System.out.println("La " + guida.toString() + " ha iniziato il tour.");
			
			
		}finally {
			l.unlock();
		}
	}

	@Override public void visitaFine() throws InterruptedException {
		l.lock();
		Thread guida = Thread.currentThread();
		try {

			possonoTerminareTour = true;
			System.out.println("La " + guida.toString() + " ha terminato il tour.");
			terminaTour.signalAll();
			
		}finally {
			l.unlock();
		}
	}

	@Override public void turistaInizia() throws InterruptedException {
		l.lock();
		Thread turista = Thread.currentThread();
		try {
			
			while(!possoUnirmiAlGruppo())
				possoUnirmiAlGruppo.await();
			
			
			System.out.println("Il "+ turista.toString() + " si e' unito al gruppo di turisti per il tour.");
			gruppo.add(turista);
			System.out.println("GRUPPO TURISTI: " + gruppo.toString());
			turisti.add(turista);
			System.out.println("TURISTI: " + turisti.toString());
			
			while(!gruppoFormato())
				gruppoFormato.await();
			
			System.out.println("Il "+ turista.toString() + " ha iniziato il tour.");
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoUnirmiAlGruppo() {
		return gruppo.size() < this.nr_turisti_per_gruppo;
	}

	@Override public void turistaFine() throws InterruptedException {
		l.lock();
		Thread turista = Thread.currentThread();
		try {
			
			while(!possoTerminareTour())
				terminaTour.await();

			System.out.println("Il "+ turista.toString() + " attende la terminazione del tour..");
			
			System.out.println("Il "+ turista.toString() + " ha terminato il tour..");
			
			gruppo.remove(turista);
			System.out.println("GRUPPO TURISTI: " + gruppo.toString());
			turisti.remove(turista);
			System.out.println("TURISTI: " + turisti.toString());
			
			this.nr_turisti--;
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoTerminareTour() {
		return possonoTerminareTour;
	}
	
	
	public static void main(String...strings) {
		int nr_turisti_per_gruppo = 20;
		TourFirenze tf = new TourFirenzeLockCondition(nr_turisti_per_gruppo);
		int nr_turisti = 20;
		tf.test(nr_turisti);
	}
	
	

}
