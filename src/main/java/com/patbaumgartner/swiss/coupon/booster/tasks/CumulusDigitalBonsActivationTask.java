package com.patbaumgartner.swiss.coupon.booster.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patbaumgartner.swiss.coupon.booster.apis.MigrosApi;
import com.patbaumgartner.swiss.coupon.booster.settings.MigrosAccountSettings;

@Slf4j
@Component
@RequiredArgsConstructor
public class CumulusDigitalBonsActivationTask implements ActivationTask {

	private final ObjectMapper objectMapper;

	private final RestTemplateBuilder restTemplateBuilder;

	private final MigrosAccountSettings migrosAccountSettings;

	@Override
	public void activate() {

		MigrosApi migrosApi = new MigrosApi(restTemplateBuilder, objectMapper, migrosAccountSettings);
		migrosApi.loginMigrosAccount();
		migrosApi.loginCumulus();
		List<String> coupons = migrosApi.collectCumulusDigitalCoupons();
		coupons.forEach(coupon -> migrosApi.activateCumulusDigitalCoupon(coupon));

		log.info("Cumulus Digital Bons Activation Task activated.");
	}

}
