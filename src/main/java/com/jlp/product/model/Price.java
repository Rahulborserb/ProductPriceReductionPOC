package com.jlp.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {

	private String was;
	private String then1;
	private String then2;
	private Object now;
	private String currency;
	
	public String getWas() {
		return was;
	}
	public void setWas(String was) {
		this.was = was;
	}
	public String getThen1() {
		return then1;
	}
	public void setThen1(String then1) {
		this.then1 = then1;
	}
	public String getThen2() {
		return then2;
	}
	public void setThen2(String then2) {
		this.then2 = then2;
	}
	
	public Object getNow() {
		return now;
	}
	public void setNow(Object now) {
		this.now = now;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	
	
	
}
