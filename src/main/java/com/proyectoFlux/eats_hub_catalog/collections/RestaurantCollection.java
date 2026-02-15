package com.proyectoFlux.eats_hub_catalog.collections;

import com.proyectoFlux.eats_hub_catalog.dtos.Review;
import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.records.Address;
import com.proyectoFlux.eats_hub_catalog.records.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "restaurants")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder

public class RestaurantCollection {
    @Id
    private UUID id;


    private String name;
    private Integer capacity;
    private Address address;
    @Indexed
    private String cuisineType;
    @Indexed
    private PriceEnum priceRange;
    private String openHours;
    private String logoUrl;
    private ContactInfo contactInfo;

    private List<Review>  reviews;
}
