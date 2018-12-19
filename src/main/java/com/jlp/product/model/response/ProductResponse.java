package com.jlp.product.model.response;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jlp.product.model.ColorSwatch;

@XmlRootElement(name = "product")
public class ProductResponse {
	
	private String productId;
	
	private String title;
	
	private List<ColorSwatch> colorSwatches;
	
	private String nowPrice;
	
	private String priceLabel;
	
	@JsonIgnore
	private Double priceReduction;

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

	public List<ColorSwatch> getColorSwatches() {
		return colorSwatches;
	}

	public void setColorSwatches(List<ColorSwatch> colorSwatches) {
		this.colorSwatches = colorSwatches;
	}

	public String getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}

	public String getPriceLabel() {
		return priceLabel;
	}

	public void setPriceLabel(String priceLabel) {
		this.priceLabel = priceLabel;
	}

	@XmlTransient
	public Double getPriceReduction() {
		return priceReduction;
	}

	public void setPriceReduction(Double priceReduction) {
		this.priceReduction = priceReduction;
	}
}
