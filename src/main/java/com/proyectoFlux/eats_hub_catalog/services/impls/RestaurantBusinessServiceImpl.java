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
    private final CatalogCacheService restaurantCache;

    @Override
    public Flux<RestaurantResponse> readAll(Integer page,Integer size) {
        log.info("reading page:{},size: {}",page,size);
        return service.readAll(page,size)
                .transform(mapper::toResponseFlux)
                .doOnComplete(()-> log.info("reading all restaurants completed"));


    }

    @Override
    public Flux<RestaurantResponse> readByCuisineType(String cuisineType) {

        final String cacheKey= CatalogCacheService.buildCuisineTypeKey(cuisineType);

        return restaurantCache.getCacheRestaurants(cacheKey)
                .switchIfEmpty(
                        this.service.readByCuisineType(cuisineType)
                                .transform(this.mapper::toResponseFlux)
                                .transform(restaurantDb-> this.restaurantCache.cacheRestaurants(cacheKey,restaurantDb))
                )
                                .doOnComplete(()-> log.info("reading restaurants by cuisine type on completed"));

    }

    @Override
    public Mono<RestaurantResponse> findByName(String name) {
        log.info("reading restaurant by name");


        final String cacheKey= CatalogCacheService.buildNameKey(name);
        return restaurantCache.getCacheRestaurant(cacheKey)
                .switchIfEmpty(
                        this.service.findByName(name)
                                .transform(this.mapper::toResponseMono)
                                .flatMap(restaurantDB ->this.restaurantCache.cacheRestaurant(cacheKey,restaurantDB))

                )
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

        final String cacheKey= CatalogCacheService.buildPriceKey(priceRange);
        return restaurantCache.getCacheRestaurants(cacheKey)
                .switchIfEmpty(
                        this.service.findByPriceRangeIn(priceRange)
                                .transform(this.mapper::toResponseFlux)
                                .transform(restaurantDB-> this.restaurantCache.cacheRestaurants(cacheKey,restaurantDB))
                )
                .doOnComplete(()-> log.info("reading restaurants by priceRange completed"));




    }

    @Override
    public Flux<RestaurantResponse> findByCity(String city) {
        log.info("reading restaurant by city {}",city);

        final String cacheKey= CatalogCacheService.buildCityKey(city);

        return restaurantCache.getCacheRestaurants(cacheKey)
                .switchIfEmpty(
                        this.service.findByCity(city)
                                .transform(this.mapper::toResponseFlux)
                                .transform(restaurantDB-> this.restaurantCache.cacheRestaurants(cacheKey,restaurantDB))

                ).doOnComplete(()-> log.info("reading restaurant by city completed"));


    }
}
