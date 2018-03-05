package com.epitools.homer.homer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@SpringBootApplication
public class HomerApplication {

	@Bean
	public static ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}

	public static void main(String[] args) {
		SpringApplication.run(HomerApplication.class, args);
	}
}
