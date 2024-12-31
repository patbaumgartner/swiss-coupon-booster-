package com.patbaumgartner.swiss.coupon.booster.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "migros.account")
public record MigrosAccountSettings(String username, String password) {
}
