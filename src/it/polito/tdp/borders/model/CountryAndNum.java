package it.polito.tdp.borders.model;

public class CountryAndNum implements Comparable<CountryAndNum>{
	
	private Country c;
	private int num;
	public CountryAndNum(Country c, int num) {
		super();
		this.c = c;
		this.num = num;
	}
	public Country getC() {
		return c;
	}
	public void setC(Country c) {
		this.c = c;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	@Override
	public int compareTo(CountryAndNum c) {
		return -(this.num-c.num);
	}
}
