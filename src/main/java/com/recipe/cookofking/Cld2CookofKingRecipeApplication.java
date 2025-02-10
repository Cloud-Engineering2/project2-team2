package com.recipe.cookofking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Cld2CookofKingRecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cld2CookofKingRecipeApplication.class, args);
	}

}
