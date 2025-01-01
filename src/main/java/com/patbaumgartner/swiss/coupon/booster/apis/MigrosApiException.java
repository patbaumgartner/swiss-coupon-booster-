package com.patbaumgartner.swiss.coupon.booster.apis;

import java.io.Serial;

public class MigrosApiException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public MigrosApiException(String message) {
		super(message);
	}

	public MigrosApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
