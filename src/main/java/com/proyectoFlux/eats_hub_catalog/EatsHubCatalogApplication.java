package com.proyectoFlux.eats_hub_catalog;

import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import com.proyectoFlux.eats_hub_catalog.repositories.ReservationRepository;
import com.proyectoFlux.eats_hub_catalog.services.definitions.ReservationCrudService;
import com.proyectoFlux.eats_hub_catalog.services.definitions.RestaurantCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class EatsHubCatalogApplication implements CommandLineRunner {


	@Autowired
	private ReservationCrudService reservationCrudService;


	public static void main(String[] args) {

		SpringApplication.run(EatsHubCatalogApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		final var parrillaModernaID = "0ee619ba-e95f-4103-99f7-ee9cdf831d90";
		final var unavailableID = "dfcbe98d-392b-4b93-9a49-27005223d15d";


        /**final var michaelReservation = createTestReservation(
                unavailableID,
                "Michael Davis",
                2,
                "2025-06-16",
                "19:00",
                "Anniversary dinner - romantic table"
        );

        final var michaelReservationCreated = reservationCrudService.createReservation(michaelReservation)
        .block();

        System.out.println("michaelReservationCreated: " + michaelReservationCreated.getId());*/

		final var michaelReservationToUpdate = reservationCrudService.readByReservationId(UUID.fromString("5e24de7c-3beb-41fd-9572-a361606835db")).block();

		michaelReservationToUpdate.setTime("18:30");
		michaelReservationToUpdate.setPartySize(3);

		final var michaelReservationUpdated = this.reservationCrudService.updateReservation(UUID.fromString("5e24de7c-3beb-41fd-9572-a361606835db"), michaelReservationToUpdate).block();

		System.out.println("michael reservation updated: " + michaelReservationUpdated.getDate());
		System.out.println("michael reservation updated: " + michaelReservationUpdated.getPartySize());

	}

	private ReservationCollection createTestReservation(String restaurantId, String customerName,
														int partySize, String date, String time, String notes) {
		return ReservationCollection.builder()
				.id(UUID.randomUUID())
				.restaurantId(restaurantId)
				.customerName(customerName)
				.partySize(partySize)
				.date(date)
				.time(time)
				.notes(notes)
				.build();
	}


	}



