package com.proyectoFlux.eats_hub_catalog.services.impls;

import com.proyectoFlux.eats_hub_catalog.dtos.request.ReservationRequest;
import com.proyectoFlux.eats_hub_catalog.dtos.responses.ReservationResponse;
import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import com.proyectoFlux.eats_hub_catalog.mappers.ReservationMapper;
import com.proyectoFlux.eats_hub_catalog.repositories.ReservationRepository;
import com.proyectoFlux.eats_hub_catalog.services.definitions.ReservationBusinessService;
import com.proyectoFlux.eats_hub_catalog.services.definitions.ReservationCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationBusinessServiceImpl implements ReservationBusinessService {

    private final ReservationCrudService service;
    private final ReservationMapper mapper;

    @Override
    public Mono<String> createReservation(ReservationRequest reservation) {
        log.info("creating reservation with: {}",reservation);
        return Mono.just(reservation)
                .transform(mapper::toCollectionMono)
                .flatMap(service::createReservation)
                .map(savedReservation->{
                    log.info("saving reservation:{}",savedReservation);
                    return savedReservation.getId().toString();
                });
    }

    @Override
    public Mono<ReservationResponse> readByReservationId(UUID id) {
        log.info("reading reservation with id {}",id);
        return service.readByReservationId(id)
                .transform(mapper::toResponseMono)
                .doOnSuccess(reservationResponse -> log.info("reading reservation with id {} successfully",id));

    }

    @Override
    public Flux<ReservationResponse> readByRestaurant(UUID id, ReservationStatusEnum status) {
        log.info("reading reservation with restaurant id");

        return service.readByRestaurant(id,status)
                .transform(mapper::toResponseFlux)
                .doOnComplete(()-> log.info("reading reservation with restaurante id {} successfully",id));
    }

    @Override
    public Mono<ReservationResponse> updateReservation(UUID id, ReservationRequest reservation) {
        log.info("updating reservation with id: {}",id);
        return Mono.just(reservation)
                .transform(mapper::toCollectionMono)
                .flatMap(reservationCollection -> service.updateReservation(id,reservationCollection))
                .transform(mapper::toResponseMono)
                .doOnNext(reservationResponse -> log.info("updating reservation with id: {}",id));
    }

    @Override
    public Mono<Void> delateReservation(UUID id) {
        log.info("delate reservation with id: {}",id);
        return service.delateReservation(id)
                .doOnSuccess(VOID-> log.info("deleting reservation successfully with id: {}",id));
    }
}
