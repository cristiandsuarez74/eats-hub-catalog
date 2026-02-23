package com.proyectoFlux.eats_hub_catalog.mappers;

import com.proyectoFlux.eats_hub_catalog.collections.ReservationCollection;
import com.proyectoFlux.eats_hub_catalog.dtos.request.ReservationRequest;
import com.proyectoFlux.eats_hub_catalog.dtos.responses.ReservationResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.spec.PSource;
import java.util.Objects;

@Mapper(componentModel ="spring")
public interface ReservationMapper {
    @Mapping(target = "dateTime",expression = "java(collection.getDate()+\",\" + collection.getTime())")
    ReservationResponse toResponse(ReservationCollection collection);

    @Mapping(target = "notes", source = "comment")
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "date",expression = "java(extractDate(request.getDateTime()))")
    @Mapping(target = "time",expression = "java(extractTime(request.getDateTime()))")
    @Mapping(target = "status",ignore = true)
    ReservationCollection collection(ReservationRequest request);

    default Flux<ReservationResponse> toResponseFlux(Flux<ReservationCollection> collection){
        return collection.map(this::toResponse);
    }
    default Mono<ReservationResponse> toResponseMono(Mono<ReservationCollection> collection){
        return collection.map(this::toResponse);
    }
    default Flux<ReservationCollection> toCollectionFlux(Flux<ReservationRequest> request){
        return request.map(this::collection);
    }
    default Mono<ReservationCollection> toCollectionMono(Mono<ReservationRequest> request){
        return request.map(this::collection);
    }

    @Named("extractDate")
    default String extractDate(String dateTime){
        return dateTime.split(",")[0];
    }

    @Named("extractTime")
    default String extractTime(String dateTime){
        return dateTime.split(",")[1];
    }


}
