package com.proyectoFlux.eats_hub_catalog.validators;

import com.mongodb.reactivestreams.client.MongoClient;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BusinessValidator<T> {
    Mono<Void> validate(T input);
}
