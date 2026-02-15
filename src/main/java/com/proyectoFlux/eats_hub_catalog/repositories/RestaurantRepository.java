package com.proyectoFlux.eats_hub_catalog.repositories;

import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantCollection, UUID> {
    Flux<RestaurantCollection> findByCuisineType(String cuisineType);
    Mono<RestaurantCollection> findByNameStartingWithIgnoreCase(String name);
    Flux<RestaurantCollection> findByPriceRangeIn(List<PriceEnum> priceRange);
    Flux<RestaurantCollection> findByAddressCity(String City);
}
