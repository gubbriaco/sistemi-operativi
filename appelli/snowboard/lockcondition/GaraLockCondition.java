package snowboard.lockcondition;

import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import snowboard.Gara;
import snowboard.Snowboarder;

public class GaraLockCondition extends Gara {
	
	private Lock l = new ReentrantLock();
	private Condition partire = l.newCondition();
	private Condition stampareInfo = l.newCondition();

	public GaraLockCondition(int N) {
		super(N);
	
		
	}

	@Override public void partenza(Snowboarder s) throws InterruptedException {
		l.lock();
		try {
			
			inAttesaDiPartire.addLast(s);
			System.out.println("IN ATTESA: " + inAttesaDiPartire);
			s.setInizio(Calendar.getInstance());
			
			
		}finally {
			l.unlock();
		}
		
	}

	@Override public int arrivo(Snowboarder s) throws InterruptedException {
		l.lock();
		try {
			
			while(!possoPartire(s))
				partire.await();
			
			s.setArrivo(Calendar.getInstance());
			int minute = Math.abs(s.getArrivo().get(Calendar.MINUTE) - s.getInizio().get(Calendar.MINUTE));
			int sec = Math.abs(s.getArrivo().get(Calendar.SECOND) - s.getInizio().get(Calendar.SECOND));
			String tempo = minute + "," + sec + " min.";
			s.setTempo(tempo);
			
			classifica.add(s);
			classifica.sort(new SnowboarderComparator());
			
			stampareInfo.signal();
			
		}finally {
			l.unlock();
		}
		return 0;
	}
	
	private boolean possoPartire(Snowboarder s) {
		return inAttesaDiPartire.getFirst().equals(s);
	}

	@Override public boolean stampaEProssimo() throws InterruptedException {
		l.lock();
		try {
			
			Snowboarder s = inAttesaDiPartire.removeFirst();
			
			while(!possoStampareInfo(s))
				stampareInfo.await();
			
			System.out.println(s.toString() + " -- " + s.getTempo());
			
			partire.signal();
			
		}finally {
			l.unlock();
		}

		return false;
	}
	
	private boolean possoStampareInfo(Snowboarder s) {
		return classifica.contains(s);
	}

	@Override public void classificaFinale() throws InterruptedException {
		l.lock();
		try {
			
			System.out.println();
			for(int i=0;i<this.N;++i) {
				Snowboarder s = this.classifica.get(i);
				System.out.println(s.toString() + "   " + s.getTempo() + "  " + i + "°");
			}
			
		}finally {
			l.unlock();
		}
		
	}

	
	public static void main(String[] args) {
		int N = 50;
		Gara gara = new GaraLockCondition(N);
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
