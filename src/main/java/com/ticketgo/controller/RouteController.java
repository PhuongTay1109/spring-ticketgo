package com.ticketgo.controller;


import com.ticketgo.dto.request.SearchRoutesRequest;
import com.ticketgo.dto.response.ApiPaginationResponse;
import com.ticketgo.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routes")
@Slf4j
public class RouteController {
    private final RouteService routeService;

    @PostMapping("/search")
    public ApiPaginationResponse searchRoutes(@Valid @RequestBody SearchRoutesRequest request) {
        return  routeService.searchRoutes(
                request.getDepartureLocation(),
                request.getArrivalLocation(),
                request.getDepartureDate(),
                request.getSortBy(),
                request.getSortDirection(),
                request.getPageNumber(),
                request.getPageSize()
        );
    }
}

