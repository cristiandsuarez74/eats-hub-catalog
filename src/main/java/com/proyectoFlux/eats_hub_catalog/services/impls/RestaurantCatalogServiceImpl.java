package com.proyectoFlux.eats_hub_catalog.services.impls;

import com.proyectoFlux.eats_hub_catalog.collections.RestaurantCollection;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.records.Address;
import com.proyectoFlux.eats_hub_catalog.repositories.RestaurantRepository;
import com.proyectoFlux.eats_hub_catalog.services.definitions.RestaurantCatalogService;
import lombok.AllArgsConstructor;
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
public class RestaurantCatalogServiceImpl implements RestaurantCatalogService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public Flux<RestaurantCollection> readAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Flux<RestaurantCollection> readByCuisineType(String cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType)
                .doOnSubscribe(subscription -> log.info("init search with param: {}",cuisineType))
                .doOnNext(restaurantCollection -> log.info("found the name with param: {}",restaurantCollection.getName()))
                .onErrorResume(erro-> {
                    log.error(erro.getMessage(),erro);
                    return Flux.empty();
                });
    }

    @Override
    public Mono<RestaurantCollection> findByName(String name) {
        return restaurantRepository.findByNameStartingWithIgnoreCase(name)
                .doOnSubscribe(subscription -> log.info("init search start with param: {}",name))
                .onErrorResume(erro-> {
                    log.error(erro.getMessage(),erro);
                    return Mono.empty();
                });
    }

    @Override
    public Flux<RestaurantCollection> findByPriceRangeIn(List<PriceEnum> priceRange) {
        return restaurantRepository.findByPriceRangeIn(priceRange)
                .switchIfEmpty(Flux.empty().cast(RestaurantCollection.class))
                .doOnSubscribe(subscription -> log.info("Restaurant is empty"));

    }

    @Override
    public Flux<RestaurantCollection> findByCity(String city) {
        return restaurantRepository.findAll()
                .map(RestaurantCollection::getAddress)
                .filter(Objects::nonNull)
                .map(Address::city)
                .filter(Objects::nonNull)
                .distinct()
                .collectList()
                .flatMapMany(cities-> {
                    if (cities.isEmpty()) {
                        log.info("no restaurants found in city:{}", city);
                    }
                    log.info("init search in the city: {}", city);
                    return restaurantRepository.findByAddressCity(city)
                            .doOnNext(restaurant -> log.info("Found restaurant in city: {} with param: {}", city, restaurant.getName()))
                            .onErrorResume(throwable -> {
                                log.error(throwable.getMessage(), throwable);
                                return Flux.empty();


                            });
                });

    }
    }

