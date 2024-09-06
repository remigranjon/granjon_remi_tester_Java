package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, boolean discount) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();
        double duration = (outHour - inHour) / (60 * 60 * 1000);
        double price = 0.0;

        if (duration >= 0.5) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    price = duration * Fare.CAR_RATE_PER_HOUR;
                    break;
                }
                case BIKE: {
                    price = duration * Fare.BIKE_RATE_PER_HOUR;
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }

        if (discount) {
            price *= 0.95;
        }

        ticket.setPrice(Math.round(price * 100) / 100.0);
    }

    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);
    }
}