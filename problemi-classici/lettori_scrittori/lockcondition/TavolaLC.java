package lettori_scrittori.lockcondition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lettori_scrittori.Tavola;

public class TavolaLC extends Tavola {

	private Lock l = new ReentrantLock();
	private Condition[] posso_mangiare;
	private boolean[] bacchette;
	
	public TavolaLC(int numero_posti) {
		super(numero_posti);
		
		posso_mangiare = new Condition[numero_posti];
		for(int i=0;i<numero_posti;++i)
			posso_mangiare[i] = l.newCondition();
		
		bacchette = new boolean[numero_posti];
		
	}

	@Override public void mangia(int i) throws InterruptedException {
		l.lock();
		try {
			
			while(bacchette[i] && bacchette[(i+1)%this.getNumeroPosti()]) {
				if(bacchette[i])
					posso_mangiare[i].await();
				else
					posso_mangiare[(i+1)%this.getNumeroPosti()].await();
			}
			
			bacchette[i] = true;
			bacchette[(i+1)%this.getNumeroPosti()] = true;
			
		}finally {
			l.unlock();
		}
	}
	

	@Override public void pensa(int i) throws InterruptedException {
		l.lock();
		try {
			
			bacchette[i] = false;
			bacchette[(i+1)%this.getNumeroPosti()] = false;
			posso_mangiare[i].notifyAll();
			posso_mangiare[(i+1)%this.getNumeroPosti()].notifyAll();
			
		}finally {
			l.unlock();
		}
	}
	
	
	public static void main(String...strings) {
		new TavolaLC(10).test(5);
	}

}
