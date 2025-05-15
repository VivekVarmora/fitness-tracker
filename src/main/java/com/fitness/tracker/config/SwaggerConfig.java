package com.fitness.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
//		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("basicAuth"))
//				.components(new Components().addSecuritySchemes("basicAuth",
//						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic").in(SecurityScheme.In.HEADER)
//								.name("Authorization")))
//				.info(new Info().title("Fitness Tracker API").version("1.0")
//						.description("API documentation for the Fitness Tracker System"));

		return new OpenAPI()
				.components(new Components().addSecuritySchemes("basicAuth",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
				.addSecurityItem(new SecurityRequirement().addList("basicAuth"))
				.info(new Info().title("Fitness Tracker API").version("1.0").description("API Documentation"));
	}

}
