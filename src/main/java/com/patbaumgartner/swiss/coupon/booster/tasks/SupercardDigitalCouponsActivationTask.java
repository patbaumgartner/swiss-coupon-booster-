package com.patbaumgartner.swiss.coupon.booster.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.stereotype.Component;

import com.patbaumgartner.swiss.coupon.booster.settings.CoopSupercardSettings;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupercardDigitalCouponsActivationTask implements ActivationTask {

	private final CoopSupercardSettings coopSupercardSettings;

	@Override
	public void activate() {
		log.info("Supercard Digital Coupons Activation Task activated.");
	}

}
