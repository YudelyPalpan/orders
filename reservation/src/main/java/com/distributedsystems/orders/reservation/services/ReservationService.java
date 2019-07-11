package com.distributedsystems.orders.reservation.services;

public interface ReservationService {
  void sendOrderToBilling(int orderId);
  
  void makeOrderReservation(int orderId);
}