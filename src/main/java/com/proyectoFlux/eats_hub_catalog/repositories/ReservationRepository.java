package com.proyectoFlux.eats_hub_catalog.repositories;

import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ReservationRepository extends ReactiveMongoRepository<ReservationCollection, UUID> {

    Flux<ReservationCollection> findByRestaurantId(String RestauranteId);
    Flux<ReservationCollection> findByRestaurantIdAndStatus(String restaurantId, ReservationStatusEnum status);
}
