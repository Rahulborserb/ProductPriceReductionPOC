package com.jlp.product.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class is used to get configuration related to Product
 * Service.
 * 
 * @author Rahul Borse
 *
 */
@Configuration
@ConfigurationProperties("product")
public class ProductServiceConfig {

	private String wasThenNowPriceLable;
	private String wasNowPriceLable;
	private String percOffPriceLable;
	private Map<String, String> colorHexaCodeMap;

	public String getWasThenNowPriceLable() {
		return wasThenNowPriceLable;
	}

	public void setWasThenNowPriceLable(String wasThenNowPriceLable) {
		this.wasThenNowPriceLable = wasThenNowPriceLable;
	}

	public String getWasNowPriceLable() {
		return wasNowPriceLable;
	}

	public void setWasNowPriceLable(String wasNowPriceLable) {
		this.wasNowPriceLable = wasNowPriceLable;
	}

	public String getPercOffPriceLable() {
		return percOffPriceLable;
	}

	public void setPercOffPriceLable(String percOffPriceLable) {
		this.percOffPriceLable = percOffPriceLable;
	}

	public Map<String, String> getColorHexaCodeMap() {
		return colorHexaCodeMap;
	}

	public void setColorHexaCodeMap(Map<String, String> colorHexaCodeMap) {
		this.colorHexaCodeMap = colorHexaCodeMap;
	}
}
