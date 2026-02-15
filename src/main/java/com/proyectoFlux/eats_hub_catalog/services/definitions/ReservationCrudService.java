package com.proyectoFlux.eats_hub_catalog.services.definitions;

import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationCrudService {
    Mono<ReservationCollection> createReservation(ReservationCollection reservation);
    Mono<ReservationCollection> readByReservationId(UUID id);
    Flux<ReservationCollection> readByRestaurant(UUID id, ReservationStatusEnum status);
    Mono<ReservationCollection> updateReservation(UUID id, ReservationCollection reservationCollection);
    Mono<Void> delateReservation(UUID id);
}
