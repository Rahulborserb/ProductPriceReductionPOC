package com.jlp.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlp.product.model.response.ProductResponsePayload;
import com.jlp.product.service.ProductService;

/**
 * This Controller class is used to exposed rest end point.
 * @author Rahul Borse
 *
 */
@RestController
public class ProductServiceController {

	@Autowired
	private ProductService productService;

	
	/**
	 * This is rest endpoint to get the product response where we can pass
	 * labelType as query parameter
	 * 
	 * @param labelType
	 * @return productResponsePayload
	 */
	@GetMapping(value = "/v2/product", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ProductResponsePayload getAllProducts(
			@RequestParam(value = "labelType", required = false) String labelType) {
		return productService.getProductResponse(labelType);
	}
}
