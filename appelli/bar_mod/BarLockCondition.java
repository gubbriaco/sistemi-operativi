package bar_mod;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarLockCondition extends Bar {
	
	private Lock l = new ReentrantLock(true);
	
	private Condition[] postoOccupato = new Condition[NUMERO_FILE];
	
	@SuppressWarnings("unchecked")
	private LinkedList<Thread>[] fila = new LinkedList[NUMERO_FILE];
	
	
	public BarLockCondition() {
		super();
		for(int i=0;i<NUMERO_FILE;++i) {
			postoOccupato[i] = l.newCondition();
			fila[i] = new LinkedList<Thread>();
		}
	}
	

	@Override public int scegli() throws InterruptedException {
		int operazione = -1;
		l.lock();
		
		try {
			
			if( numeroPostiLiberi[CASSA] == 0 )
				operazione = BANCONE;
			else if( numeroPostiLiberi[BANCONE] == 0 )
				operazione = CASSA;
			else if( fila[CASSA].size() <= fila[BANCONE].size() )
				operazione = CASSA;
			else
				operazione = BANCONE;
			
			System.out.println( "Il " + Thread.currentThread().toString() + " vuole andare " +
							  "verso " + (operazione==CASSA?" la cassa":" il bancone") );
			System.out.println(this);
		}finally {
			l.unlock();
		}
		return operazione;
	}

	@Override public void inizia(int i) throws InterruptedException {
		Thread current = Thread.currentThread();
		l.lock();
		
		try {
			
			fila[i].add(current);
			
			while( !mioTurno(i, current) )
				postoOccupato[i].await();
			
			fila[i].remove(current);
			numeroPostiLiberi[i]--;
			
			System.out.println("Il " + Thread.currentThread().toString() + " si trova " +
					   (i==CASSA?"in cassa":"al bancone"));
			System.out.println(this);
		}finally {
			l.unlock();
		}
	}
	
	private boolean mioTurno(int operazione, Thread current) {
		return fila[operazione].indexOf(current) < numeroPostiLiberi[operazione];
	}

	@Override public void finisci(int i) throws InterruptedException {
		l.lock();
		
		try {
		
			numeroPostiLiberi[i]++;
			
			if( fila[i].size()>0 )
				postoOccupato[i].signalAll();
			
			System.out.println("Il " + Thread.currentThread().toString() + " ha terminato " +
					   (i==CASSA?"alla cassa":"al bancone"));
			System.out.println("Il " + Thread.currentThread().toString() + " esce dal bar");
			System.out.println(this);
		}finally {
			l.unlock();
		}
	}
	
	
	public String toString() {
		String cassa = numeroPostiLiberi[CASSA]==0?"X":" ";
		String bancone = "";
		for(int i=0; i<MAX_PERSONE_PER_FILA[BANCONE]; i++){
			if( i < MAX_PERSONE_PER_FILA[BANCONE]-numeroPostiLiberi[BANCONE] )
				bancone +="X";
			else
				bancone +=" ";
		}
		String tFila[] = new String[NUMERO_FILE];
		for (int i = 0; i < tFila.length; i++) {
			tFila[i] = "";
			for(Thread t: fila[i])
				tFila[i]+=t.getName()+" ";
			tFila[i] = tFila[i].trim();
		}
		
		String ret = String.format("CASSA[%s](%s) BANCONE[%s](%s)",
				cassa, tFila[CASSA],
				bancone, tFila[BANCONE]);
		return ret;
	}
	
	
	public static void main(String...strings) {
		
		Bar bar = new BarLockCondition();
		bar.test(100);
		
	}

	
}
