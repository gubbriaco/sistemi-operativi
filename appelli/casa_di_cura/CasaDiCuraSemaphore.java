package casa_di_cura;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class CasaDiCuraSemaphore extends CasaDiCura {

	/** per modificare le variabili interne */
	private Semaphore mutex = new Semaphore(1); 
	
	private Semaphore prossimoPazienteInPreparazione = new Semaphore(3);
	private Semaphore possoEntrareInSalaOperatoria = new Semaphore(1, true);
	
	private Semaphore possoUscire = new Semaphore(0);
	
	private Semaphore possoOperare = new Semaphore(0);
	
	private LinkedList<Thread> pazientiInFila;
	private LinkedList<Thread> pazientiInPreparazione;
	
	
	public CasaDiCuraSemaphore(int dimensioneSalaPreparazione) {
		super(dimensioneSalaPreparazione);
		pazientiInFila = new LinkedList<>();
		pazientiInPreparazione = new LinkedList<>();
	}

	
	/**
	 * Sospende il paziente fin quando non entra in sala operatoria.
	 */
	@Override public void pazienteEntra() throws InterruptedException {
		
		mutex.acquire();
		Thread paziente = Thread.currentThread();
		
		if( pazientiInPreparazione.size()<3 ) {
			pazientiInPreparazione.add(paziente);
			System.out.println("Il " + paziente.toString() + " e' entrato in sala preparazione.");
			System.out.println("SALA = " + pazientiInPreparazione.toString());
		}
		else {
			pazientiInFila.add(paziente);
			System.out.println("Il " + paziente.toString() + " e' in FILA.");
			System.out.println("FILA = " + pazientiInFila.toString());
		}

		prossimoPazienteInPreparazione.acquire();
		mutex.release();
		
		possoOperare.release();
		possoEntrareInSalaOperatoria.acquire();
		
	}

	/**
	 * Il medico chiama un nuovo paziente e attende che il paziente sia entrato 
	 * in sala operatoria. Dopo essersi assicurato che il paziente &egrave in sala 
	 * operatoria inizia l'operazione.
	 */
	@Override public void chiamaEIniziaOperazione() throws InterruptedException {
		
		mutex.acquire();
		Thread pazienteDaOperare = pazientiInPreparazione.removeFirst();
		Thread pazienteInFila = pazientiInFila.removeFirst();
		pazientiInPreparazione.addLast(pazienteInFila);
		prossimoPazienteInPreparazione.release();
		
		Thread medico = Thread.currentThread();
		mutex.release();

		System.out.println("Il " + pazienteDaOperare.toString() + " sta entrando in sala operatoria..");
		
		possoOperare.acquire();
		System.out.println("Il " + medico.toString() + " sta operando il " + pazienteDaOperare.toString());
		
	}

	/**
	 * Il medico segnala al paziente che l'operazione &egrave conclusa e che 
	 * pu&ograve lasciare l'ospedale. Non deve attendere l'uscita del paziente 
	 * dalla salava operatoria.
	 */
	@Override public void fineOperazione() throws InterruptedException {
		
		Thread medico = Thread.currentThread();
		System.out.println("Il " + medico.toString() + " ha terminato l'operazione.");

		possoEntrareInSalaOperatoria.release();
		possoUscire.release();

		
	}

	/**
	 * Segnala al paziente che ha concluso l'operazione e che pu&ograve uscire
	 * dalla sala operatoria.
	 */
	@Override public void pazienteEsci() throws InterruptedException {
		Thread pazienteOperato = Thread.currentThread();
		System.out.println("Il " + pazienteOperato.toString() + " sta uscendo..");
		possoUscire.acquire();
	}
	
	
	public static void main(String...strings) {
		
		int dimensioneSalaPreparazione = 3;
		int numeroPazienti = 100;
		CasaDiCura cdc = new CasaDiCuraSemaphore(dimensioneSalaPreparazione);
		cdc.test(numeroPazienti);
		
	}

	
}
