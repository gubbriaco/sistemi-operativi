package lettori_scrittori.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lettori_scrittori.Tavola;

public class TavolaLC extends Tavola {

	private Lock l = new ReentrantLock();
	private Condition possoMangiare = l.newCondition(),
					  possoPensare = l.newCondition();
	private int[] tavola;
	
	private LinkedList<Thread> vogliono_mangiare, stanno_mangiando,
							   vogliono_pensare, stanno_pensando;
	
	public TavolaLC(int numero_posti) {
		super(numero_posti);
		
		tavola = new int[numero_posti];
		for(int i=0;i<numero_posti;++i)
			tavola[i] = 1;
		
		vogliono_mangiare = new LinkedList<>();
		stanno_mangiando = new LinkedList<>();
		vogliono_pensare = new LinkedList<>();
		stanno_pensando = new LinkedList<>();
	}

	@Override public void mangia(int i) throws InterruptedException {
		l.lock();
		Thread filosofo = Thread.currentThread();
		try {
			
			vogliono_mangiare.addLast(filosofo);
			
			while(!possoMangiare(filosofo, i))
				possoMangiare.await();
			
			vogliono_mangiare.remove(filosofo);
			tavola[i] = 0;
			tavola[(i+1)%tavola.length] = 0;
			stanno_mangiando.addLast(filosofo);
			
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoMangiare(Thread filosofo, int i) {
		return vogliono_mangiare.getFirst().equals(filosofo) &&
			   tavola[i] == 1 && tavola[(i+1)%tavola.length] == 1;
	}
	

	@Override public void pensa(int i) throws InterruptedException {
		l.lock();
		Thread filosofo = Thread.currentThread();
		try {
			
			vogliono_pensare.addLast(filosofo);
			
			while(!possoPensare(filosofo, i))
				possoPensare.await();
			
			vogliono_pensare.remove(filosofo);
			tavola[i] = 1;
			tavola[(i+1)%tavola.length] = 1;
			stanno_mangiando.remove(filosofo);
			stanno_pensando.addLast(filosofo);
			
			possoMangiare.signalAll();
			possoPensare.signalAll();
			
		}finally {
			l.unlock();
		}
	}
	
	private boolean possoPensare(Thread filosofo, int i) {
		return vogliono_pensare.getFirst().equals(filosofo) &&
			   tavola[i] == 0 && tavola[(i+1)%tavola.length] == 0;
	}
	
	
	public static void main(String...strings) {
		new TavolaLC(10).test(5);
	}

}
