package com.driver.model;

import javax.persistence.*;

@Entity
@Table
public class Cab {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Id;

	private int perKmRate;

	private boolean available;

	@OneToOne
	@JoinColumn
	private Driver driver;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getPerKmRate() {
		return perKmRate;
	}

	public void setPerKmRate(int perKmRate) {
		this.perKmRate = perKmRate;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Cab() {
		this.perKmRate=10;
		this.available=true;
	}

	public Cab(int Id, int perKmRate, boolean available, Driver driver) {
		this.Id = Id;
		this.perKmRate = perKmRate;
		this.available = available;
		this.driver = driver;
	}

}