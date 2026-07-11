package com.proyectoFlux.eats_hub_catalog.services.definitions;

import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantCatalogService {
    Flux<RestaurantCollection> readAll(Integer page, Integer size);
    Flux<RestaurantCollection> readByCuisineType(String cuisineType);
    Mono<RestaurantCollection> findByName(String name);
    Flux<RestaurantCollection> findByPriceRangeIn(List<PriceEnum> priceRange);
    Flux<RestaurantCollection> findByCity(String city);
}
