package com.ticketgo.service;

import com.ticketgo.model.Customer;
import com.ticketgo.model.Ticket;

import java.util.List;

public interface TicketService {
    void reserveSeats(String ticketCode, long customerId);

    List<Ticket> findReservedTickets(long userId);

    void saveAll(List<Ticket> tickets);

    boolean existsReservedSeatsByCustomer(Customer customer);

    void releaseReservedSeatsByCustomer(long customerId);

    boolean isSeatAvailable( long seatId, long scheduleId);

    List<Ticket> findAllByScheduleId(long scheduleId);

    double getPriceBySeatIdAndScheduleId(long scheduleId, long seatId);

    Ticket findByTicketCode(String ticketCode);

    List<Ticket> findAllByBookingId(long bookingId);
}