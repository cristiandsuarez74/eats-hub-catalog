package com.proyectoFlux.eats_hub_catalog.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private String restaurantId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String dateTime; // example 2025-06-16,15:30
    private Integer partySize;
    private String comment;
}
