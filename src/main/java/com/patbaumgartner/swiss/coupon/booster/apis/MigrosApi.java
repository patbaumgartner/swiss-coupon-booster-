package com.patbaumgartner.swiss.coupon.booster.apis;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patbaumgartner.swiss.coupon.booster.settings.MigrosAccountSettings;

@Slf4j
public class MigrosApi {

	private MigrosAccountSettings settings;

	private RestTemplate restTemplate;

	private ObjectMapper objectMapper;

	public MigrosApi(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper,
			MigrosAccountSettings migrosAccountSettings) {
		this.settings = migrosAccountSettings;
		this.objectMapper = objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		this.restTemplate = restTemplateBuilder.requestFactory(() -> new HttpComponentsClientHttpRequestFactory(
				HttpClients.custom().setDefaultCookieStore(new BasicCookieStore()).setUserAgent("Mozilla/5.0").build()))
			.build();
	}

	public void loginMigrosAccount() {
		// Step 1: Call to login into Migros Account

		// Step 1.1: Get CSRF token by sending a GET request
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(settings.loginUrl(), HttpMethod.GET, entity,
				String.class);
		String responseBody = response.getBody();

		// Step 1.2: Extract CSRF token
		Document document = Jsoup.parse(responseBody);
		Elements metaElements = document.select("meta[name=_csrf]");

		String csrfToken = null;
		if (!metaElements.isEmpty()) {
			Element metaElement = metaElements.first();
			csrfToken = metaElement.attr("content");
			System.out.println("Found CSRF token: " + csrfToken);
		}
		else {
			throw new MigrosApiException("CSRF token not found.");
		}

		// Step 1.3: Authenticate using the CSRF token, username, and password
		HttpHeaders loginHeaders = new HttpHeaders();
		loginHeaders.set("Content-Type", "application/x-www-form-urlencoded");

		String body = "_csrf=" + csrfToken + "&username=" + settings.username() + "&password=" + settings.password();
		HttpEntity<String> loginEntity = new HttpEntity<>(body, loginHeaders);

		ResponseEntity<String> loginResponse = restTemplate.exchange(settings.loginUrl(), HttpMethod.POST, loginEntity,
				String.class);

		if (loginResponse.getStatusCode() != HttpStatus.OK) {
			throw new MigrosApiException("Authentication failed. Please check your credentials.");
		}

		log.info("Successfully logged into Migros account.");

	}

	public void loginCumulus() {
		// Step 2: Send GET request to login to Cumulus after successful authentication
		HttpHeaders cumulusHeaders = new HttpHeaders();
		cumulusHeaders.set("Accept", "text/html");

		HttpEntity<String> cumulusEntity = new HttpEntity<>(cumulusHeaders);

		ResponseEntity<String> cumulusResponse = restTemplate.exchange(settings.cumulusLoginUrl(), HttpMethod.GET,
				cumulusEntity, String.class);

		if (cumulusResponse.getStatusCode() != HttpStatus.OK) {
			throw new MigrosApiException("Cumulus login failed.");
		}

		log.info("Successfully logged into Cumulus account.");
	}

	@SneakyThrows
	public List<String> collectCumulusDigitalCoupons() {

		// Step 3: Send GET request to collect Cumulus Digital Coupons
		HttpHeaders collectHeaders = new HttpHeaders();
		collectHeaders.set("Accept", "text/html");

		HttpEntity<String> collectEntity = new HttpEntity<>(collectHeaders);

		ResponseEntity<String> collectResponse = restTemplate.exchange(settings.cumulusCouponsUrl(), HttpMethod.GET,
				collectEntity, String.class);

		if (collectResponse.getStatusCode() != HttpStatus.OK) {
			throw new MigrosApiException("Digital coupons collection failed.");
		}

		// Step 3.1: Extract digital bons from the response
		List<String> digitalCoupons = new ArrayList<>();
		Document document = Jsoup.parse(collectResponse.getBody());

		// Select all <article> elements
		for (Element article : document.select("article")) {
			// Extract the value of the data-setup attribute
			String dataSetup = article.attr("data-setup");

			// Parse the JSON part of the data-setup using Jackson
			JsonNode rootNode = objectMapper.readTree(dataSetup);
			JsonNode dataNode = rootNode.path("data");
			JsonNode promotionsNode = dataNode.path("promotions");

			// If there are promotions in the array, extract the details
			if (promotionsNode.isArray()) {
				for (JsonNode promotion : promotionsNode) {
					String id = promotion.path("id").asText();
					String name = promotion.path("name").asText();

					digitalCoupons.add(id);
					log.info("Found digital coupon: {} - {}", id, name);
				}
			}
		}

		log.info("Successfully collected {} ditigal coupons.", digitalCoupons.size());

		return digitalCoupons;
	}

	public void activateCumulusDigitalCoupon(String id) {
		// Step 4: Send POST request to activate the digital coupon

		HttpHeaders activationHeaders = new HttpHeaders();
		activationHeaders.set("Accept", "*/*");

		HttpEntity<String> activationEntity = new HttpEntity<>(activationHeaders);

		ResponseEntity<String> activationResponse = restTemplate.exchange(settings.cumulusCouponsActivationUrl(),
				HttpMethod.POST, activationEntity, String.class, id);

		if (activationResponse.getStatusCode() != HttpStatus.OK) {
			throw new MigrosApiException("Activation failed.");
		}
	}

}
