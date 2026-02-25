package com.proyectoFlux.eats_hub_catalog;

import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.dtos.Review;
import com.proyectoFlux.eats_hub_catalog.dtos.responses.ReservationResponse;
import com.proyectoFlux.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import com.proyectoFlux.eats_hub_catalog.mappers.ReservationMapper;
import com.proyectoFlux.eats_hub_catalog.mappers.RestaurantMapper;
import com.proyectoFlux.eats_hub_catalog.records.Address;
import com.proyectoFlux.eats_hub_catalog.records.ContactInfo;
import com.proyectoFlux.eats_hub_catalog.repositories.ReservationRepository;
import com.proyectoFlux.eats_hub_catalog.services.definitions.ReservationCrudService;
import com.proyectoFlux.eats_hub_catalog.services.definitions.RestaurantCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class EatsHubCatalogApplication implements CommandLineRunner {




	public static void main(String[] args) {

		SpringApplication.run(EatsHubCatalogApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

	}

	}



