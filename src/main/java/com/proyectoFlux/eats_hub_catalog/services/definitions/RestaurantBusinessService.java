package com.proyectoFlux.eats_hub_catalog.services.definitions;

import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantBusinessService {
    Flux<RestaurantResponse> readAll(Integer page, Integer size);

    Flux<RestaurantResponse> readByCuisineType(String cuisineType);

    Mono<RestaurantResponse> findByName(String name);

    Flux<RestaurantResponse> findByPriceRangeIn(List<PriceEnum> priceRange);

    Flux<RestaurantResponse> findByCity(String city);
}
