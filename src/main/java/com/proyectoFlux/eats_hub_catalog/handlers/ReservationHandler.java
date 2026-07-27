package com.proyectoFlux.eats_hub_catalog.handlers;

import com.proyectoFlux.eats_hub_catalog.dtos.request.ReservationRequest;
import com.proyectoFlux.eats_hub_catalog.services.definitions.ReservationBusinessService;
import com.proyectoFlux.eats_hub_catalog.validators.ReactiveValidator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.shaded.freemarker.core.ReturnInstruction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReservationHandler {

    private final ReservationBusinessService reservationBusinessService;
    private final ReactiveValidator validator;

    public Mono<ServerResponse>  postReservation(ServerRequest request){
        return request.bodyToMono(ReservationRequest.class)
                .flatMap(validator::validate)
                .flatMap(reservationBusinessService::createReservation)
                .flatMap(id->
                    ServerResponse
                            .created(URI.create("/reservation/"+id))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("Resource","/reservation/"+id))
                            .doOnSuccess(response->log.info("Reservation Created: {}",response))
                            .doOnError(error->log.error("Error while creating Reservation with error: {}",error.getMessage()))

                );
    }
    public Mono<ServerResponse>  getReservationById(ServerRequest request){
        final var id=request.pathVariable("id");
        return this.parseUUID(id)
                .flatMap(reservationBusinessService::readByReservationId)
                .flatMap(reservationResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservationResponse))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSuccess(response->log.info("reservation found: {}",response))
                .doOnError(error->log.info("Error with reading reservation error: {}",error.getMessage()));
    }
    public Mono<ServerResponse>  updateReservation(ServerRequest request){
        final var id=request.pathVariable("id");
        return this.parseUUID(id)
                .flatMap(uuid -> request.bodyToMono(ReservationRequest.class)
                        .flatMap(validator::validate)
                        .flatMap(reservationReq -> reservationBusinessService.updateReservation(uuid,reservationReq))
                        .flatMap(updateReservation->ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updateReservation))
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .doOnSuccess(response->log.info("Reservation update : {}",response))
                        .doOnError(error->log.info("Error with update reservation: {}",error.getMessage()))
                );

    }
    public Mono<ServerResponse>  delateReservation(ServerRequest request){
        final var id=request.pathVariable("id");
        return this.parseUUID(id)
                .flatMap(reservationBusinessService::delateReservation)
                .then(ServerResponse.noContent().build())
                .doOnSuccess(serverResponse -> log.info("delate reservation: {}",serverResponse))
                .doOnError(error-> log.error("Error while deleting reservation: {}",error.getMessage()));
    }
    public Mono<UUID> parseUUID(String uuid){
        try {
            return Mono.just(UUID.fromString(uuid));
        }catch (IllegalArgumentException e){
            return Mono.error(new ValidationException("invalided UUID"+uuid));

        }
    }
}
