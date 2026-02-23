package com.proyectoFlux.eats_hub_catalog.dtos.responses;

import com.proyectoFlux.eats_hub_catalog.enums.PriceEnum;
import com.proyectoFlux.eats_hub_catalog.records.Address;
import com.proyectoFlux.eats_hub_catalog.records.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private String name;
    private Address address;
    private String cuisineType;
    private PriceEnum priceRange;
    private String openHours;
    private String logoUrl;
    private String closeAt;
    private ContactInfo contactInfo;
    private Double globalRating;
}
