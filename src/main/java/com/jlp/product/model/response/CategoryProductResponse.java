package com.jlp.product.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jlp.product.model.CategoryProduct;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryProductResponse {

	private List<CategoryProduct> products;

	public List<CategoryProduct> getProducts() {
		return products;
	}

	public void setProducts(List<CategoryProduct> products) {
		this.products = products;
	}
	
	
}
