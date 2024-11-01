package com.ticketgo.controller;

import com.ticketgo.dto.response.ApiResponse;
import com.ticketgo.dto.response.RouteStopResponse;
import com.ticketgo.service.RouteStopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/route-stops")
public class RouteStopController {
    private final RouteStopService routeStopService;
    @GetMapping()
    public ApiResponse routeStops(@RequestParam long scheduleId) {
        RouteStopResponse resp = routeStopService.getRouteStops(scheduleId);
        return new ApiResponse(HttpStatus.OK, "Get route stops", resp);
    }
}