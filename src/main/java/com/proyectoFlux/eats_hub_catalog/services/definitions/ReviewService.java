package com.proyectoFlux.eats_hub_catalog.services.definitions;

import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.dtos.Review;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReviewService {

    public Mono<RestaurantCollection> addRestaurantReview(UUID idRestaurant, Review review);
}
