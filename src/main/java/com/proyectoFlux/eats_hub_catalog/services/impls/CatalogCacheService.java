package com.proyectoFlux.eats_hub_catalog.services.impls;

import com.proyectoFlux.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.Event.KEY_PRESS;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogCacheService {
    private final ReactiveRedisTemplate<String, RestaurantResponse> redisTemplate;
    private final ReactiveRedisTemplate<String, List<RestaurantResponse>> redisListTemplate;

    private static final Duration DEFAULT_TTL=Duration.ofHours(1);
    private static final String KEY_PREFIX="restaurant:";

    public Mono<RestaurantResponse> getCacheRestaurant(String key){
        return redisTemplate
                .opsForValue().get(key)
                .doOnNext(restaurantResponse-> log.info("Get cache restaurant"+restaurantResponse.getName()))
                .doOnSubscribe(subscription -> log.info("looking restaurant with key: {}",key));

    }
    public Mono<RestaurantResponse> cacheRestaurant(String key, RestaurantResponse restaurant){
        return this.redisTemplate
                .opsForValue()
                .set(KEY_PREFIX+key,restaurant,DEFAULT_TTL)
                .thenReturn(restaurant)
                .doOnSubscribe(subscription -> log.info("cache restaurant {}",restaurant.getName()));
    }
    public Flux<RestaurantResponse> getCacheRestaurants(String key){
        return redisListTemplate.opsForValue()
                .get(KEY_PREFIX+key)
                .flatMapMany(Flux::fromIterable)
                .doOnNext(response-> log.debug("cache hit fot list key: {}",key))
                .doOnSubscribe(subscription -> log.debug("looking in cache for list key: {}",key));
    }
    public Flux<RestaurantResponse> cacheRestaurants(String key, Flux<RestaurantResponse> restaurants){
        return restaurants.collectList()
                .flatMap(list-> redisListTemplate.opsForValue()
                        .set(KEY_PREFIX+key,list,DEFAULT_TTL)
                        .thenReturn(list))
                .flatMapMany(Flux::fromIterable)
                .doOnComplete(() -> log.debug("cache restaurant list with key: {}",key));
    }
    public Mono<Boolean>evictCacheRestaurant(String key){
        return redisTemplate
                .delete(KEY_PREFIX+key)
                .map(count-> count >0)
                .doOnNext(isDelate->{
                    if (isDelate){
                        log.info("cache evicted restaurant with key: {}",key);
                    }
                });
    }
    public Mono<Void>evictCacheAllRestaurant(){
        return this.redisTemplate.getConnectionFactory()
                .getReactiveConnection()
                .serverCommands()
                .flushAll()
                .then(Mono.fromRunnable(()->log.info("cache evicted all restaurants")));

    }
    public static String buildNameKey(String name){
        return "name:"+name.toLowerCase();
    }
    public static String buildCuisineTypeKey(String cuisineType){
        return "Cuisine:"+cuisineType.toLowerCase();
    }
    public static String buildCityKey(String city){
        return "city:"+city.toLowerCase();
    }
    public static String buildPriceKey(List<PriceEnum> prices){
        String pricesList=prices.stream()
                .map(PriceEnum::toString)
                .collect(Collectors.joining(","));
        return "prices"+pricesList;
    }


}
