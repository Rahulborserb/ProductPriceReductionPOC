package com.jlp.product.service;

import static com.jlp.product.constant.ProductServiceConstant.SHOW_PERC_DISCOUNT;
import static com.jlp.product.constant.ProductServiceConstant.SHOW_WAS_NOW;
import static com.jlp.product.constant.ProductServiceConstant.SHOW_WAS_THEN_NOW;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.jlp.product.config.ProductServiceConfig;
import com.jlp.product.config.ProxyConfiguration;
import com.jlp.product.constant.ProductServiceConstant;
import com.jlp.product.model.CategoryColorSwatch;
import com.jlp.product.model.CategoryProduct;
import com.jlp.product.model.ColorSwatch;
import com.jlp.product.model.Price;
import com.jlp.product.model.response.CategoryProductResponse;
import com.jlp.product.model.response.ProductResponse;
import com.jlp.product.model.response.ProductResponsePayload;

/**
 * This service class is used to provide business logic to get Product Response
 * 
 * @author Rahul Borse
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProxyConfiguration proxyConfiguration;

	@Autowired
	private ProductServiceConfig productServiceConfig;

	/**
	 * This method is used to get the Product Response transform from Category
	 * Product Response.
	 * 
	 * @param request
	 * @param response
	 * @param labelType
	 * @return productPayLoad
	 */
	public ProductResponsePayload getProductResponse(String labelType) {

		CategoryProductResponse categoryProductResponse = getCategoryProducts(); 
		ProductResponsePayload productPayLoad = new ProductResponsePayload();
		List<ProductResponse> productResponseList = new ArrayList<>();

		if (categoryProductResponse != null && !CollectionUtils.isEmpty(categoryProductResponse.getProducts())) {
			for (CategoryProduct categoryProduct : categoryProductResponse.getProducts()) {
				// Skip the product which has empty was price or now price is as
				// an object
				if ((categoryProduct.getPrice().getNow() instanceof String)
						&& !StringUtils.isEmpty(categoryProduct.getPrice().getWas())) {
					ProductResponse productResponse = new ProductResponse();
					productResponse.setProductId(categoryProduct.getProductId());
					productResponse.setTitle(categoryProduct.getTitle());
					productResponse.setNowPrice(getPriceFormatForGBP(categoryProduct.getPrice().getNow().toString()));
					setProductPriceLable(productResponse, categoryProduct.getPrice(), labelType);
					setColorSwatches(productResponse, categoryProduct.getColorSwatches());
					productResponse.setPriceReduction(getPriceReduction(categoryProduct.getPrice()));
					productResponseList.add(productResponse);
				}
			}
			// Sort the products based on highest price reduction first
			productResponseList.sort(Comparator.comparing(ProductResponse::getPriceReduction));
		}
		productPayLoad.setProducts(productResponseList);
		return productPayLoad;
	}

	/**
	 * This method is used to set Color Swatches to Product Response
	 * 
	 * @param productResponse
	 * @param categoryColorSwatches
	 */
	private void setColorSwatches(ProductResponse productResponse, List<CategoryColorSwatch> categoryColorSwatches) {
		List<ColorSwatch> colorSwatches = new ArrayList<>();
		for (CategoryColorSwatch categoryColorSwatch : categoryColorSwatches) {
			ColorSwatch colorSwatch = new ColorSwatch();
			colorSwatch.setColor(categoryColorSwatch.getColor());
			// Set the rbg color as hexa code of color and it will come from
			// color map defined in application.propertieshighest price
			// reduction first.
			colorSwatch
					.setRgbColor(productServiceConfig.getColorHexaCodeMap().get(categoryColorSwatch.getBasicColor()));
			colorSwatch.setSkuId(categoryColorSwatch.getSkuId());
			colorSwatches.add(colorSwatch);
		}
		productResponse.setColorSwatches(colorSwatches);
	}

	/**
	 * This method is used to set Price label to Product Response
	 * 
	 * @param productResponse
	 * @param price
	 * @param labelType
	 */
	private void setProductPriceLable(ProductResponse productResponse, Price price, String labelType) {
		if (StringUtils.isEmpty(labelType)) {
			//If labelType is empty, assign default labelType as ShowWasNow
			labelType = SHOW_WAS_NOW; 
		}
		String wasPrice = getPriceFormatForGBP(price.getWas());
		String then1Price = getPriceFormatForGBP(price.getThen1());
		String then2Price = getPriceFormatForGBP(price.getThen2());
		String nowPrice = getPriceFormatForGBP(price.getNow().toString());

		switch (labelType) {
		case SHOW_WAS_NOW:
			setPriceLabelsToProduct(productResponse, wasPrice, null, nowPrice);
			break;
		case SHOW_WAS_THEN_NOW:
			if (!StringUtils.isEmpty(price.getThen2())) {
				setPriceLabelsToProduct(productResponse, wasPrice, then2Price, nowPrice);
			} else if (!StringUtils.isEmpty(price.getThen1())) {
				setPriceLabelsToProduct(productResponse, wasPrice, then1Price, nowPrice);
			} else {
				setPriceLabelsToProduct(productResponse, wasPrice, null, nowPrice);
			}
			break;
		case SHOW_PERC_DISCOUNT:
			double discountPercentage = getPriceReduction(price);
			DecimalFormat df = new DecimalFormat(ProductServiceConstant.DOUBLE_FORMAT_RULE);
			setPriceLabelForPercentOff(productResponse, df.format(discountPercentage), nowPrice);
			break;
		default:
			setPriceLabelsToProduct(productResponse, wasPrice, null, nowPrice);

		}
	}

	/**
	 * This method is used to get Category Product Response.
	 * 
	 * @return categoryProductResponse
	 */
	private CategoryProductResponse getCategoryProducts() {
		restTemplate = getRestTemplate(proxyConfiguration.getReadTimeout(), proxyConfiguration.getConnectTimout());
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		// headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<CategoryProductResponse> responseEntity = restTemplate.exchange(
				proxyConfiguration.getDomainUrl() + proxyConfiguration.getUri(), HttpMethod.GET, entity,
				CategoryProductResponse.class);

		System.out.println("responseEntity:" + responseEntity.getBody());
		return responseEntity.getBody();
	}

	/**
	 * This method is used to prepare restTemplate for rest call to fetch
	 * Category Product Response.
	 * 
	 * @param readtTimeout
	 * @param connectTimeout
	 * @return restTemplate
	 */
	public RestTemplate getRestTemplate(int readtTimeout, int connectTimeout) {
		HttpComponentsClientHttpRequestFactory componentsClientHttpRequestFactory = getHttpComponentsClientHttpRequestFactory(
				readtTimeout, connectTimeout);
		restTemplate = new RestTemplate(componentsClientHttpRequestFactory);
		return restTemplate;
	}

	/**
	 * This method is used to prepare HttpComponentsClientHttpRequestFactory
	 * object which is being used in RestTemplate.
	 * 
	 * @param requestTimeout
	 * @param connectTimeout
	 * @return
	 */
	private HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory(int requestTimeout,
			int connectTimeout) {
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder = requestBuilder.setConnectTimeout(connectTimeout);
		requestBuilder = requestBuilder.setSocketTimeout(requestTimeout);
		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		if (proxyConfiguration.isProxyEnabled()) {
			HttpHost proxy = new HttpHost(proxyConfiguration.getHost(), Integer.parseInt(proxyConfiguration.getPort()));
			requestBuilder.setProxy(proxy);
		}
		httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());
		CloseableHttpClient httpClient = httpClientBuilder.build();
		final HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);
		return httpComponentsClientHttpRequestFactory;
	}

	/**
	 * This method is used to get the Price in GBP format.
	 * 
	 * @param price
	 * @return priceInString
	 */
	private String getPriceFormatForGBP(String price) {
		StringBuilder priceInfo = new StringBuilder(ProductServiceConstant.G_B_P);
		String formattedPrice = ProductServiceConstant.ZERO;
		if (!StringUtils.isEmpty(price)) {
			Double priceInDouble = Double.valueOf(price);
			if (priceInDouble < 10) {
				DecimalFormat df = new DecimalFormat(ProductServiceConstant.DECIMAL_FORMAT);
				formattedPrice = df.format(priceInDouble);
			} else {
				formattedPrice = price;
			}
		}
		return priceInfo.append(formattedPrice).toString();

	}

	/**
	 * This method is used to construct Price labels based on WasPrice,ThenPrice
	 * and NowPrice.
	 * 
	 * @param productResponse
	 * @param wasPrice
	 * @param thenPrice
	 * @param nowPrice
	 */
	private void setPriceLabelsToProduct(ProductResponse productResponse, String wasPrice, String thenPrice,
			String nowPrice) {
		String formattedMessage = null;
		Object[] paramsArr = null;
		MessageFormat messageFormat = null;
		// messageFormat will get message from application.properties file and
		// prepare price lable according to parameter passed
		if (StringUtils.isEmpty(thenPrice)) {
			paramsArr = new Object[] { wasPrice, nowPrice };
			messageFormat = new MessageFormat(productServiceConfig.getWasNowPriceLable());
		} else {
			paramsArr = new Object[] { wasPrice, thenPrice, nowPrice };
			messageFormat = new MessageFormat(productServiceConfig.getWasThenNowPriceLable());
		}
		formattedMessage = messageFormat.format(paramsArr);
		productResponse.setPriceLabel(formattedMessage);
	}

	/**
	 * This method is used to construct Price labels based on PercentOff.
	 * 
	 * @param productResponse
	 * @param discountPercentage
	 * @param nowPrice
	 */
	private void setPriceLabelForPercentOff(ProductResponse productResponse, String discountPercentage,
			String nowPrice) {
		String formattedMessage = null;
		// messageFormat will get message from application.properties file and
		// prepare price lable for percentOff according to parameter passed
		Object[] paramsArr = { discountPercentage, nowPrice };
		MessageFormat messageFormat = new MessageFormat(productServiceConfig.getPercOffPriceLable());
		formattedMessage = messageFormat.format(paramsArr);
		productResponse.setPriceLabel(formattedMessage);
	}

	/**
	 * This method is used to get Price Reduction based which is calculated
	 * based on discount percentage.
	 * 
	 * @param price
	 * @return
	 */
	private double getPriceReduction(Price price) {
		double wasValue = Double.parseDouble(price.getWas());
		double nowValue = Double.parseDouble(price.getNow().toString());
		return wasValue == 0 ? 0 : ((wasValue - nowValue) / (wasValue)) * 100;
	}
}
