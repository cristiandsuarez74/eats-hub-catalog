package com.proyectoFlux.eats_hub_catalog.services.impls;

import com.proyectoFlux.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.mappers.RestaurantMapper;
import com.proyectoFlux.eats_hub_catalog.repositories.RestaurantRepository;
import com.proyectoFlux.eats_hub_catalog.services.definitions.RestaurantBusinessService;
import com.proyectoFlux.eats_hub_catalog.services.definitions.RestaurantCatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantBusinessServiceImpl implements RestaurantBusinessService {

    private final RestaurantCatalogService service;
    private final RestaurantMapper mapper;

    @Override
    public Flux<RestaurantResponse> readAll() {
        log.info("reading all restaurants");
        return service.readAll()
                .transform(mapper::toResponseFlux)
                .doOnComplete(()-> log.info("reading all restaurants completed"));
    }

    @Override
    public Flux<RestaurantResponse> readByCuisineType(String cuisineType) {

        log.info("reading restaurants by cuisineType {}",cuisineType);
        return service.readByCuisineType(cuisineType)
                .transform(mapper::toResponseFlux)
                .doOnComplete(()-> log.info("reading restaurants by cuisine type on completed"));
    }

    @Override
    public Mono<RestaurantResponse> findByName(String name) {
        log.info("reading restaurant by name");
        return service.findByName(name)
                .transform(mapper::toResponseMono)
                .doOnSuccess(restaurant->{
                    if (Objects.isNull(restaurant)){
                        log.info("reading restaurants by name not found any restaurants");

                    }else {
                        log.info("reading restaurant by name completed");
                    }
                });
    }

    @Override
    public Flux<RestaurantResponse> findByPriceRangeIn(List<PriceEnum> priceRange) {
        log.info("reading restaurant by priceRange {}",priceRange);
        return service.findByPriceRangeIn(priceRange)
                .transform(mapper::toResponseFlux)
                .doOnComplete(()-> log.info("reading restaurants by priceRange completed"));
    }

    @Override
    public Flux<RestaurantResponse> findByCity(String city) {
        log.info("reading restaurant by city {}",city);
        return service.findByCity(city)
                .transform(mapper::toResponseFlux)
                .doOnComplete(()-> log.info("reading restaurant by city completed"));

    }
}
