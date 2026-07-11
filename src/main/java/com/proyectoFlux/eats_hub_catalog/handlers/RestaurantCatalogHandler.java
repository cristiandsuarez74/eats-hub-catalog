package com.proyectoFlux.eats_hub_catalog.handlers;

import com.proyectoFlux.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.services.definitions.RestaurantBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RestaurantCatalogHandler {

    private final RestaurantBusinessService restaurantBusinessService;

    public Mono<ServerResponse> getAllRestaurant(ServerRequest serverRequest){
        final Integer page= Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        final Integer size= Integer.parseInt(serverRequest.queryParam("size").orElse("10"));


        final var restaurantFlux=restaurantBusinessService.readAll(page,size);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(restaurantFlux, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> getRestaurantByName(ServerRequest serverRequest){
        final var restaurantName= serverRequest.pathVariable("name");
        final var monoResponse=restaurantBusinessService.findByName(restaurantName);
        return monoResponse
                .flatMap(restaurantResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(restaurantResponse)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> getRestaurantByCuisineType(ServerRequest serverRequest){
        final var cousinType=serverRequest.queryParam("cousinType").orElse(null);
        if (Objects.isNull(cousinType)){
            return ServerResponse.badRequest().bodyValue("cousinType is requerid");
        }
        final var fluxResponse=restaurantBusinessService.readByCuisineType(cousinType);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> getRestaurantBetweenPrice(ServerRequest serverRequest){
        final var priceRange=serverRequest.queryParam("prices").orElse(null);
        if (Objects.isNull(priceRange)){
            return ServerResponse.badRequest().bodyValue("priceRange is requerid");
        }
        final var typesList= Arrays.stream(priceRange.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(PriceEnum::valueOf)
                .toList();
        final var fluxResponse=restaurantBusinessService.findByPriceRangeIn(typesList);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> getRestaurantByCity(ServerRequest serverRequest) {
        final var city = serverRequest.queryParam("city").orElse(null);
        if (Objects.isNull(city)) {
            return ServerResponse.badRequest().bodyValue("city  is requerid");
        }
        final var fluxResponse = restaurantBusinessService.findByCity(city);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
