package com.jlp.product.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryProduct {

	private String productId;
	
	private String title;
	
	private List<CategoryColorSwatch> colorSwatches;
	
	private Price price;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<CategoryColorSwatch> getColorSwatches() {
		return colorSwatches;
	}

	public void setColorSwatches(List<CategoryColorSwatch> colorSwatches) {
		this.colorSwatches = colorSwatches;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	
	
	

}
