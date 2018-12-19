package com.jlp.product.service;

import com.jlp.product.model.response.ProductResponsePayload;

public interface ProductService {
	public ProductResponsePayload getProductResponse(String labelType);
}
