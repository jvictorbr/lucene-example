package com.jv.console.impl.city;

public class City {
	
	private String name;
	private String state;
	private Long population;
	
	public City(String name, String state, Long population) {
		this.name = name;
		this.state = state;
		this.population = population;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getPopulation() {
		return population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}
	
	

}
