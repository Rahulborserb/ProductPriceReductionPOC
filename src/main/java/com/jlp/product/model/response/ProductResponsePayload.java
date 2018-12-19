package com.jlp.product.model.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payload")
public class ProductResponsePayload {
	
	private List<ProductResponse> products;

	@XmlElement(name = "product")
	public List<ProductResponse> getProducts() {
		return products;
	}

	public void setProducts(List<ProductResponse> products) {
		this.products = products;
	}

}
