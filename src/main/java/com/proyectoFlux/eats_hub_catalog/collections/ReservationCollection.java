package com.proyectoFlux.eats_hub_catalog.collections;

import com.proyectoFlux.eats_hub_catalog.enums.ReservationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Document(collection = "reservations")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor

public class ReservationCollection {
    @Id
    private UUID id;
    @Indexed
    private String restaurantId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String time;
    private Integer partySize;
    @Indexed
    private ReservationStatusEnum status;
    private String notes;
}
