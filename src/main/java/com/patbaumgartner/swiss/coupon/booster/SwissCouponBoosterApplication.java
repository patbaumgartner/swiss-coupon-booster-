package com.patbaumgartner.swiss.coupon.booster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.patbaumgartner.swiss.coupon.booster.settings.CoopSupercardSettings;
import com.patbaumgartner.swiss.coupon.booster.settings.MigrosAccountSettings;

@SpringBootApplication
@EnableConfigurationProperties({ MigrosAccountSettings.class, CoopSupercardSettings.class })
public class SwissCouponBoosterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwissCouponBoosterApplication.class, args);
	}

}
