package it.polito.tdp.borders.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	//parametri di simulazione
	private int INIZIALI=1000;
	private double perc_stanziali=0.5;
	
	//modello del mondo: numero attaccato a ciascuna country
	private Map<Country,Integer> stanziali;
	private UndirectedGraph<Country,DefaultEdge> grafo;
	
	//evoluzione: calcola le misure in uscita -> passi
	private int passi;
	
	//coda degli eventi
	private PriorityQueue<Evento> coda;

	public Simulatore(UndirectedGraph<Country,DefaultEdge> grafo ) {
		//inizializzazione variabili di lavoro: mappa coda passi
		this.grafo=grafo;
		
		this.stanziali=new HashMap<Country,Integer>();
		//inizializzo a 0 gli stanziali di ogni stato
		for(Country c: grafo.vertexSet()){
			stanziali.put(c, 0);
		}
		
		coda=new PriorityQueue<Evento>();
		
	}
	
	public void inserisci(Country c){
		//primo country che si visita
		Evento e=new Evento(INIZIALI, c,1);
		coda.add(e);
	}
	
	public void run(){
		passi=0;
		
		while(!coda.isEmpty()){
			Evento e=coda.poll();
			passi=e.getT();//l'ultimo che estraggo sara sicuramente l'ultimo t e quindi anche il maggiore
			
			//stanziali
			int stanz=(int) (e.getNum()*this.perc_stanziali);//arrotondamento per difetto
			
			//emigranti
			int confinanti=Graphs.neighborListOf(grafo,e.getCountry()).size();
			int nomadi=(e.getNum()-stanz)/confinanti;
			
			//aggiungismo i resti della divisione precedente perche non è detto che sia un valore intero x ogni country
			stanz=e.getNum()-nomadi*confinanti;
			
			//aggiornare il modello del mondo
			//contabilizzare questi stanziali
			this.stanziali.put(e.getCountry(), this.stanziali.get(e.getCountry())+stanz);
			
			//non inserisco nella coda degli eventi che hanno 0 persone
			if(nomadi>0){
				//schedulare gli eventi fututi
				//inserire destinazione dei nomadi
				for(Country c: Graphs.neighborListOf(grafo,e.getCountry())){
					Evento ev=new Evento(nomadi,c,e.getT()+1);
					coda.add(ev);
				}
			}
		}
	}
	
	public int getPassi(){
		return passi;
	}
	
	public List<CountryAndNum> getStanziali() {
		List<CountryAndNum> lista=new ArrayList<>();
		
		for(Country c: this.stanziali.keySet()){
			if(stanziali.get(c)>0){
				lista.add(new CountryAndNum(c,stanziali.get(c)));
			}
		}
		Collections.sort(lista);
		return lista;
	}
}
