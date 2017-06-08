package it.polito.tdp.borders.model;

public class Evento implements Comparable<Evento>{
	
	//solo un tipo di evento: ingresso
	
	private int num;//quante persone
	private Country country;//in quale stato
	private int t;//in quale istante di tempo
	
	public Evento(int num, Country country, int t) {
		super();
		this.num = num;
		this.country = country;
		this.t = t;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	@Override
	public int compareTo(Evento o) {
		return this.t-o.t;
	}
}
