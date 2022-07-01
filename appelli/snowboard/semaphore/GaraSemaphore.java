package snowboard.semaphore;

import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.Semaphore;

import snowboard.Gara;
import snowboard.Snowboarder;

public class GaraSemaphore extends Gara {
	
	private Semaphore mutex = new Semaphore(1);
	
	private Semaphore prossimo = new Semaphore(1);
	private Semaphore possoPartire = new Semaphore(1);
	private Semaphore parti = new Semaphore(1);
	private Semaphore stampaInfo = new Semaphore(0);
	
	
	
	public GaraSemaphore(int N) {
		super(N);
	
		
	}

	@Override public void partenza(Snowboarder s) throws InterruptedException {

		prossimo.acquire();
		
		mutex.acquire();
		inAttesaDiPartire.addLast(s);
		System.out.println("IN ATTESA: " + inAttesaDiPartire);
		s.setInizio(Calendar.getInstance());
		mutex.release();
		
		prossimo.release();
		possoPartire.acquire();

	}

	@Override public int arrivo(Snowboarder s) throws InterruptedException {
		
		parti.acquire();
		
		mutex.acquire();
		s.setArrivo(Calendar.getInstance());
		int minute = Math.abs(s.getArrivo().get(Calendar.MINUTE) - s.getInizio().get(Calendar.MINUTE));
		int sec = Math.abs(s.getArrivo().get(Calendar.SECOND) - s.getInizio().get(Calendar.SECOND));
		String tempo = minute + "," + sec + " min.";
		s.setTempo(tempo);
		
		stampaInfo.release();
		
		classifica.add(s);
		classifica.sort(new SnowboarderComparator());
		mutex.release();
		
		return 0;
	}

	@Override public boolean stampaEProssimo() throws InterruptedException {

		possoPartire.release();
		
		stampaInfo.acquire();
		
		mutex.acquire();
		Snowboarder s = inAttesaDiPartire.removeFirst();
		System.out.println(s.toString() + " -- " + s.getTempo());
		mutex.release();
		
		parti.release();
		
		return false;
	}

	@Override public void classificaFinale() throws InterruptedException {
		
		mutex.acquire();
		System.out.println();
		for(int i=0;i<this.N;++i) {
			Snowboarder s = this.classifica.get(i);
			System.out.println(s.toString() + "   " + s.getTempo() + "  " + i + "°");
		}
		mutex.release();
		
	}

	
	public static void main(String[] args) {
		int N = 50;
		Gara gara = new GaraSemaphore(N);
		gara.test();
	}
	
	
	class SnowboarderComparator implements Comparator<Snowboarder> {
		@Override public int compare(Snowboarder s1, Snowboarder s2) {
			if(s1.getArrivo().get(Calendar.MINUTE) > s2.getArrivo().get(Calendar.MINUTE))
				return -1;
			else if(s1.getArrivo().get(Calendar.MINUTE) < s2.getArrivo().get(Calendar.MINUTE))
				return 1;
			return 0;
		}
	}

}
