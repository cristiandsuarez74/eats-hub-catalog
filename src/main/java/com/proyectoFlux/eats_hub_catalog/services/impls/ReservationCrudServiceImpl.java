package com.proyectoFlux.eats_hub_catalog.services.impls;

import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import com.proyectoFlux.eats_hub_catalog.exceptions.ResourceNotFoundException;
import com.proyectoFlux.eats_hub_catalog.repositories.ReservationRepository;
import com.proyectoFlux.eats_hub_catalog.repositories.RestaurantRepository;
import com.proyectoFlux.eats_hub_catalog.services.definitions.ReservationCrudService;
import com.proyectoFlux.eats_hub_catalog.validators.ReservationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationCrudServiceImpl implements ReservationCrudService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationValidator reservationValidator;

    @Override
    public Mono<ReservationCollection> createReservation(ReservationCollection reservation) {
        reservation.setId(UUID.randomUUID());
        final var validations= List.of(
                reservationValidator.validateRestaurantNotClosed(),
                reservationValidator.validateAvailability()
        );
        return this.reservationValidator.applyValidations(reservation,validations)
                .then(
                        restaurantRepository.findById(UUID.fromString(reservation.getRestaurantId()))
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("restauran no found")))
                )
                .flatMap(restaurant->{
                    if (Objects.isNull(reservation
                            .getStatus())){
                        reservation.setStatus(ReservationStatusEnum.PENDING);
                    }
                    log.info("creating reservation with id; {}",reservation.getId());
                    return reservationRepository.save(reservation);
                });
    }

    @Override
    public Mono<ReservationCollection> readByReservationId(UUID id) {
        return reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Id no encontrado")));
    }

    @Override
    public Flux<ReservationCollection> readByRestaurant(UUID id, ReservationStatusEnum status) {
        return restaurantRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("restauran no found")))
                .flatMapMany(restaurant->{
                    if (Objects.isNull(status)){
                        log.info("Reading reservation with id: {}",restaurant.getId());
                        return reservationRepository.findByRestaurantId(id.toString());
                    }
                    log.info("Reading reservation with id {} and status {}",restaurant.getId(),status);
                    return reservationRepository.findByRestaurantIdAndStatus(id.toString(),status);
                });
    }

    @Override
    public Mono<ReservationCollection> updateReservation(UUID id, ReservationCollection reservationCollection) {
        final var validations= List.of(
                reservationValidator.validateRestaurantNotClosed()


        );
        return reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("restauran no found")))
                .flatMap(existingReservation->{
                    reservationCollection.setRestaurantId(existingReservation.getRestaurantId());
                    return reservationValidator.applyValidations(reservationCollection,validations)
                            .thenReturn(existingReservation);
                })
                .flatMap(existing->{
                    log.info("update reservation with id: {}",existing.getId());
                    existing.setStatus(reservationCollection.getStatus());
                    existing.setDate(reservationCollection.getDate());
                    existing.setNotes(reservationCollection.getNotes());
                    existing.setTime(reservationCollection.getTime());
                    existing.setCustomerName(reservationCollection.getCustomerName());
                    existing.setPartySize(reservationCollection.getPartySize());
                    return reservationRepository.save(existing);

                });
    }

    @Override
    public Mono<Void> delateReservation(UUID id) {
        return reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("restauran no found")))
                .flatMap(reservationRepository::delete);

    }
}
