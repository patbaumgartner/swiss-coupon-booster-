package com.patbaumgartner.swiss.coupon.booster.apis;

import java.io.Serial;

public class MigrosDigitalCouponsApiException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public MigrosDigitalCouponsApiException(String message) {
		super(message);
	}

	public MigrosDigitalCouponsApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
