package com.patbumgartner.swiss.coupon.booster.runner;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CuponActivationRunner {

	@Job(name = "Cupon Activation Job")
	@Recurring(id = "cupon-activation-job", cron = "*/2 * * * *")
	public void recurringJob() throws InterruptedException {
		log.info("The cupon activation Job has begun.");
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			log.error("Error while executing cupon activation job", e);
			throw e;
		}
		finally {
			log.info("The cupon activation job has finished...");
		}
	}

}
