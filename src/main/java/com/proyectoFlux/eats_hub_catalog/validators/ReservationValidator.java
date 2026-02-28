package com.proyectoFlux.eats_hub_catalog.validators;

import com.proyectoFlux.eats_hub_catalog.clientes.PlannerMsClient;
import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.exceptions.BusinessException;
import com.proyectoFlux.eats_hub_catalog.exceptions.ResourceNotFoundException;
import com.proyectoFlux.eats_hub_catalog.repositories.ReservationRepository;
import com.proyectoFlux.eats_hub_catalog.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationValidator {

    private final PlannerMsClient plannerMsClient;
    private final RestaurantRepository restaurantRepository;

    public <T> Mono<Void> applyValidations(T input, List<BusinessValidator<T>> validations) {
        if (validations.isEmpty()){
            return Mono.empty();
        }
        return validations.stream()
                .reduce(
                        Mono.empty(),
                        (chain,validator)->chain.then(validator.validate(input)),
                        Mono::then
                );
    }

    public BusinessValidator<ReservationCollection> validateRestaurantNotClosed() {
        log.info("validating restaurant not close");
        return reservation ->{
            final var restauranteId= UUID.fromString(reservation.getRestaurantId());
            return restaurantRepository.findById(restauranteId)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .flatMap(restaurant->{
                        if (isRestaurantClose(restaurant,reservation.getTime())){
                            return Mono.error(new BusinessException("Restaurante already closed"));

                        }
                        return Mono.empty();
                    });
        };
    }

    public BusinessValidator<ReservationCollection> validateAvailability(){
        log.info("Validating availability");
        return reservation->{
            final var restauranteId= UUID.fromString(reservation.getRestaurantId());
            return plannerMsClient.verifyAvailability(reservation.getDate(),reservation.getTime(),restauranteId)
                    .flatMap(isAvailability ->{
                        if(!isAvailability){
                            return Mono.error(new BusinessException("restaurant is no availability"));

                        }
                        return Mono.empty();

                    });

        };


    }
    private Boolean isRestaurantClose(RestaurantCollection restaurant, String reservationTime){
        try {
            if (Objects.isNull(restaurant.getCloseAt())||Objects.isNull(reservationTime)){
                return true;
            }
            LocalTime closedLocalTime=LocalTime.parse(restaurant.getCloseAt(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime reservationLocalTime=LocalTime.parse(reservationTime, DateTimeFormatter.ofPattern("HH:mm"));

            return reservationLocalTime.isAfter(closedLocalTime);
        }catch (Exception e){
            log.error("error is verify closed time: {}",e.getMessage());
            return true;
        }
    }

}
