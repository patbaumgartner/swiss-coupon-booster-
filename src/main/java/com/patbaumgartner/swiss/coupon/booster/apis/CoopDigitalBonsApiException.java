package com.patbaumgartner.swiss.coupon.booster.apis;

import java.io.Serial;

public class CoopDigitalBonsApiException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public CoopDigitalBonsApiException(String message) {
		super(message);
	}

	public CoopDigitalBonsApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
