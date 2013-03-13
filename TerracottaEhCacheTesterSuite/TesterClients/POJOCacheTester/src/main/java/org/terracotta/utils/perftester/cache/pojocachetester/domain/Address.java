package org.terracotta.utils.perftester.cache.pojocachetester.domain;

import java.io.Serializable;

public class Address implements Serializable {
	private static final long serialVersionUID = -6705540997879341956L;

	private String line1;
	private String line2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private AddressCategory addressCategory;

	public Address(){
		super();
	}

	public Address(String line1, String line2, String city, String state, String zip, String country, AddressCategory addressCategory) {
		this.line1 = line1;
		this.line2 = line2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
		this.addressCategory = addressCategory;
	}

	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public AddressCategory getAddressCategory() {
		return addressCategory;
	}
	public void setAddressCategory(AddressCategory addressCategory) {
		this.addressCategory = addressCategory;
	}
}