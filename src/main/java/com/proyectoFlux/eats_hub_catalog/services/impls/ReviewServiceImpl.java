package com.proyectoFlux.eats_hub_catalog.services.impls;

import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.dtos.Review;
import com.proyectoFlux.eats_hub_catalog.repositories.RestaurantRepository;
import com.proyectoFlux.eats_hub_catalog.services.definitions.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;
    @Override
    public Mono<RestaurantCollection> addRestaurantReview(UUID idRestaurant, Review review) {
        log.info("Adding review for restaurant: {}",idRestaurant);
        return this.restaurantRepository.findById(idRestaurant)
                .switchIfEmpty(Mono.error(new RuntimeException("Restaurant not found")))
                .flatMap(restaurantDb->{
                    if (Objects.isNull(restaurantDb.getReviews())){
                        restaurantDb.setReviews(new ArrayList<>());
                    }
                    restaurantDb.getReviews().add(review);
                    log.info("Restaurant review {} added",idRestaurant);
                    return this.restaurantRepository.save(restaurantDb);
                })
                .doOnSuccess(restaurantCollection -> log.info("Restaurant {} updated success",idRestaurant))
                .doOnError(error->log.error("Restaurant {} error",idRestaurant,error));
    }
}
