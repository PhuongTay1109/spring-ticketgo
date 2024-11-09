package com.ticketgo.controller;

import com.ticketgo.dto.RouteStopDTO;
import com.ticketgo.dto.response.ApiResponse;
import com.ticketgo.dto.response.RouteStopResponse;
import com.ticketgo.service.RouteStopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/route-stops")
public class RouteStopController {
    private final RouteStopService routeStopService;

    @GetMapping()
    public ApiResponse getRouteStops(@RequestParam long scheduleId) {
        RouteStopResponse resp = routeStopService.getRouteStops(scheduleId);
        return new ApiResponse(HttpStatus.OK, "Get route stops", resp);
    }

    @GetMapping("/pickup")
    public ApiResponse getPickupStops(@RequestParam long scheduleId) {
        List<RouteStopDTO> resp = routeStopService.getPickupStops(scheduleId);
        return new ApiResponse(HttpStatus.OK, "Get pick up stops", resp);
    }

    @GetMapping("/dropoff")
    public ApiResponse getDropoffStops(@RequestParam long scheduleId) {
        List<RouteStopDTO> resp = routeStopService.getDropoffStops(scheduleId);
        return new ApiResponse(HttpStatus.OK, "Get drop off stops", resp);
    }
}
