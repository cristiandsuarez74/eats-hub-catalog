package com.proyectoFlux.eats_hub_catalog.services.definitions;

import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.dtos.request.ReservationRequest;
import com.proyectoFlux.eats_hub_catalog.dtos.responses.ReservationResponse;
import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationBusinessService {

    Mono<String> createReservation(ReservationRequest reservation);

    Mono<ReservationResponse> readByReservationId(UUID id);

    Flux<ReservationResponse> readByRestaurant(UUID id, ReservationStatusEnum status);

    Mono<ReservationResponse> updateReservation(UUID id, ReservationRequest reservation);

    Mono<Void> delateReservation(UUID id);
}
