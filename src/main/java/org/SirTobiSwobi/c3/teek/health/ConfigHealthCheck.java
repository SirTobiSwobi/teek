package org.SirTobiSwobi.c3.teek.health;

import org.SirTobiSwobi.c3.teek.TeekConfiguration;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;

public class ConfigHealthCheck extends HealthCheck {
	
	private final TeekConfiguration config;	

	public ConfigHealthCheck(TeekConfiguration config) {
		super();
		this.config = config;
	}

	@Override
	protected Result check() throws Exception {
		
		if(config.getCalls().length==0){
			 return Result.unhealthy("According to configuration metadata, this service doesn't include any calls");
		}
		return Result.healthy();
	}

}
