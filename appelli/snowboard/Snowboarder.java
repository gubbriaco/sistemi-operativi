package snowboard;

import java.util.Calendar;
import java.util.Random;

public class Snowboarder extends Thread {
	
	private int nrMaglia;
	private Gara g;
	private Calendar inizio, arrivo;
	private String tempo;
	
	private final static int MIN_TEMPO_TRAGUARDO = 1000, MAX_TEMPO_TRAGUARDO = 5000;
	private Random random;
	
	public Snowboarder(int nrMaglia, Gara g) {
		this.nrMaglia = nrMaglia;
		this.g = g;
		
		inizio = Calendar.getInstance();
		arrivo = Calendar.getInstance();
		tempo = "";
		random = new Random();
	}
	
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}
	
	public String getTempo() {
		return tempo;
	}
	
	public void setInizio(Calendar c) {
		inizio = c;
	}
	
	public void setArrivo(Calendar c) {
		arrivo = c;
	}
	
	public Calendar getInizio() {
		return inizio;
	}
	
	public Calendar getArrivo() {
		return arrivo;
	}
	
	@Override public void run() {
		try {
			
			System.out.println("Lo " + this.toString() + " si prepara a partire..");
			g.partenza(this);
			System.out.println("Lo " + this.toString() + " e' partito.");
			
			tempoPerArrivareAlTraguardo();
			
			g.arrivo(this);
			System.out.println("Lo " + this.toString() + " ha tagliato il traguardo!");
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void tempoPerArrivareAlTraguardo() throws InterruptedException {
		int tempo = random.nextInt( MIN_TEMPO_TRAGUARDO, MAX_TEMPO_TRAGUARDO+1 );
		Thread.sleep( tempo );
	}
	
	
	@Override public String toString() {
		return "Snowboarder " + nrMaglia;
	}
	
}
