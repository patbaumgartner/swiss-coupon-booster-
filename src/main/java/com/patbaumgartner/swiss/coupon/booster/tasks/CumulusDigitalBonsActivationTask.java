package com.patbaumgartner.swiss.coupon.booster.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.stereotype.Component;

import com.patbaumgartner.swiss.coupon.booster.settings.MigrosAccountSettings;

@Slf4j
@Component
@RequiredArgsConstructor
public class CumulusDigitalBonsActivationTask implements ActivationTask {

	private final MigrosAccountSettings migrosAccountSettings;

	@Override
	public void activate() {
		log.info("Cumulus Digital Bons Activation Task activated.");
	}

}
