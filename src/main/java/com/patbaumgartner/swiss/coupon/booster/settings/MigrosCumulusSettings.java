package com.patbaumgartner.swiss.coupon.booster.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "migros.cumulus")
public record MigrosCumulusSettings(String loginUrl, String couponsUrl, String couponsActivationUrl, boolean enabled) {
}
