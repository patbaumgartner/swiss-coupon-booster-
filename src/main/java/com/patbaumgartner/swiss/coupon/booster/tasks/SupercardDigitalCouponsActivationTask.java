package com.patbaumgartner.swiss.coupon.booster.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patbaumgartner.swiss.coupon.booster.apis.CoopDigitalBonsApi;
import com.patbaumgartner.swiss.coupon.booster.settings.CoopSupercardSettings;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupercardDigitalCouponsActivationTask implements ActivationTask {

	private final ObjectMapper objectMapper;

	private final RestClient.Builder restClientBuilder;

	private final CoopSupercardSettings coopSupercardSettings;

	@Override
	public void execute() {

		if (!coopSupercardSettings.enabled()) {
			log.info("Supercard Digital Coupons Activation Task is disabled.");
			return;
		}

		CoopDigitalBonsApi coopDigitalBonsApi = new CoopDigitalBonsApi(restClientBuilder, objectMapper,
				coopSupercardSettings);
		coopDigitalBonsApi.loginSupercardAccount();
		log.info("Supercard Digital Coupons activated.");
	}

}
