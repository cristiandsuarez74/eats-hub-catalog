package com.proyectoFlux.eats_hub_catalog;

import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.services.definitions.RestaurantCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class EatsHubCatalogApplication implements CommandLineRunner {

	@Autowired
	private RestaurantCatalogService service;
	public static void main(String[] args) {

		SpringApplication.run(EatsHubCatalogApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		service.findByCity("Austin")
				.doOnNext(System.out::println)
				.subscribe();

	}
}
