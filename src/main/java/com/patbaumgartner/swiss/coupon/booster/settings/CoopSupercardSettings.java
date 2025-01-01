package com.patbaumgartner.swiss.coupon.booster.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "coop.supercard")
public record CoopSupercardSettings(String username, String password, String loginUrl, boolean enabled) {
}
