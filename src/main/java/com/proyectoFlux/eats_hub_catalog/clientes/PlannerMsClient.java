package com.proyectoFlux.eats_hub_catalog.clientes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class PlannerMsClient {

    private static final String UNAVAILABLE_RESTAURANT_ID="dfcbe98d-392b-4b93-9a49-27005223d15d"; //Always is full

    public Mono<Boolean> verifyAvailability(String date, String time, UUID restaurantId){
        return Mono.fromCallable(()->!UNAVAILABLE_RESTAURANT_ID.equals(restaurantId.toString()))
                .delayElement(this.getRandomDuration())
                .doOnNext(reservation->log.info("checking availability fot restaurant {}, date {}, time {}",restaurantId,date,time));
    }
    private Duration getRandomDuration(){
        final var randomInt= ThreadLocalRandom.current().nextInt(20,1000);
        return Duration.ofMillis(randomInt);
    }

}
