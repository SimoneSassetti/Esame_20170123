package it.polito.tdp.borders.model;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private UndirectedGraph<Country, DefaultEdge> grafo;
	private List<Country> listaCountry;
	private BordersDAO dao;
	private Map<Integer, Country> countryMap;//LA USO X NON FARE UNA QUERY DIFFICILE. INFATTI MI FACCIO TORNARE SOLO DUE INTERI E NON I COUNTRY
	
	List<CountryAndNum> stanziali;
	
	public Model(){
		dao=new BordersDAO();
	}
	
	public List<CountryAndNum> creaGrafo(int anno){
		//COSI' LO CREO OGNI VOLTA X OGNI ANNO SE CAMBIA
		grafo=new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		
		//AGGIUNGO VERTICI
		BordersDAO dao=new BordersDAO();
		listaCountry=dao.loadAllCountries();
		Graphs.addAllVertices(grafo, listaCountry);
		
		countryMap=new HashMap<>();
		for(Country c: listaCountry){
			countryMap.put(c.getcCode(), c);
		}
		
		//AGGIUNGO ARCHI
		List<IntegerPair> confini=dao.getCountryPairs(anno);//funziona anche se non troviamo nessun confine se l'anno selezionato è troppo piccolo
		for(IntegerPair i: confini){
			grafo.addEdge(countryMap.get(i.getN1()), countryMap.get(i.getN2()));
		}
		
		//COSTUISCO LA LISTA DI CountryAndNum PER POTERLI STAMPARE
		List<CountryAndNum> lista=new ArrayList<CountryAndNum>();
		for(Country c: listaCountry){
			int confinanti=Graphs.neighborListOf(grafo, c).size();
			if(confinanti!=0)
				lista.add(new CountryAndNum(c,confinanti));
		}
		Collections.sort(lista);
		
		return lista;
	}
	
	public int simula(Country partenza) {
		
		Simulatore sim=new Simulatore(grafo);
		
		sim.inserisci(partenza);
		
		sim.run();
		
		stanziali=sim.getStanziali();
		
		return sim.getPassi();
	}
	
	public List<CountryAndNum> getStanziali() {
		return stanziali;
	}
		
	public static void main(String args[]){
		Model m=new Model();
		List<CountryAndNum> l=m.creaGrafo(2000);
		System.out.println(m.grafo);
		
		m.creaGrafo(1900);
		System.out.println(m.grafo);
		
		for(CountryAndNum c: l){
			System.out.format("%s: %d\n", c.getC().toString(),c.getNum());
		}
	}
}
