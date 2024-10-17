package com.ticketgo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder(toBuilder=true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "routes")
public class Route extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer routeId;

    private String routeName;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private Set<RouteStop> stops;

    @Transient
    private Set<RouteStop> pickupStops;

    @Transient
    private Set<RouteStop> dropOffStops;

    public void setStops(Set<RouteStop> stops) {
        this.stops = stops;
        populateStops();
    }

    private void populateStops() {
        pickupStops = stops.stream()
                .filter(stop -> stop.getStopType() == StopType.PICKUP)
                .collect(Collectors.toSet());
        dropOffStops = stops.stream()
                .filter(stop -> stop.getStopType() == StopType.DROPOFF)
                .collect(Collectors.toSet());
    }
}