package com.jlp.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class is use to get configuration related to proxy and
 * enpoints.
 * 
 * @author Rahul Borse
 *
 */
@Configuration
@ConfigurationProperties("jlp.proxy")
public class ProxyConfiguration {
	private String domainUrl;
	private String uri;
	private String host;
	private String port;
	private boolean isProxyEnabled;
	private int readTimeout;
	private int connectTimout;

	public String getDomainUrl() {
		return domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isProxyEnabled() {
		return isProxyEnabled;
	}

	public void setProxyEnabled(boolean isProxyEnabled) {
		this.isProxyEnabled = isProxyEnabled;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getConnectTimout() {
		return connectTimout;
	}

	public void setConnectTimout(int connectTimout) {
		this.connectTimout = connectTimout;
	}

}
