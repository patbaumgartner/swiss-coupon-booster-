package com.patbaumgartner.swiss.coupon.booster.apis;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patbaumgartner.swiss.coupon.booster.settings.CoopSupercardSettings;

@Slf4j
public class CoopDigitalBonsApi {

	private CoopSupercardSettings supercardSettings;

	private ObjectMapper objectMapper;

	private RestClient restClient;

	public CoopDigitalBonsApi(RestClient.Builder restClientBuilder, ObjectMapper objectMapper,
			CoopSupercardSettings supercardSettings) {
		this.supercardSettings = supercardSettings;
		this.objectMapper = objectMapper;
		this.restClient = restClientBuilder.requestFactory(new HttpComponentsClientHttpRequestFactory(
				HttpClients.custom().setDefaultCookieStore(new BasicCookieStore()).setUserAgent("Mozilla/5.0").build()))
			.build();
	}

	public void loginSupercardAccount() {
		// Step 1: Call to login into Supercard Account

		// Step 1.1: Get execution token by sending a GET request
		String responseBody = restClient.get()
			.uri(supercardSettings.loginUrl())
			.accept(MediaType.TEXT_HTML)
			.retrieve()
			.body(String.class);

		// Step 1.2: Extract execution token
		Document document = Jsoup.parse(responseBody);
		Elements metaElements = document.select("input[name=execution]");

		String execution = null;
		if (!metaElements.isEmpty()) {
			Element metaElement = metaElements.first();
			execution = metaElement.attr("value");
		}
		else {
			throw new CoopDigitalBonsApiException("Execution token not found.");
		}

		// Step 1.3: Authenticate using the execution token, username, and password
		String body = "execution=" + execution + "&username=" + supercardSettings.username() + "&password=" +
				supercardSettings.password();

		ResponseEntity<String> loginResponse = restClient.post()
			.uri(supercardSettings.loginUrl())
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(body)
			.retrieve()
			.toEntity(String.class);

		if (loginResponse.getStatusCode() != HttpStatus.OK) {
			throw new CoopDigitalBonsApiException("Authentication failed. Please check your credentials.");
		}

		log.info("Successfully logged into Supercard account.");

	}

}
