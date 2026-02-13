package com.proyectoFlux.eats_hub_catalog.records;

import lombok.Builder;

@Builder
public record Address(
        String street,
        String city,
        String postalCode
) {
}
