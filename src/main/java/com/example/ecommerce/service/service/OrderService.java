package com.example.ecommerce.service.service;

import com.example.ecommerce.domain.Order;
import com.example.ecommerce.dto.response.Response;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<Response> createOrder(Order order    );

    ResponseEntity<Response> deleteOrderById(Long orderId);

    ResponseEntity<Response> updateOrder(Long orderId, Order order);

    ResponseEntity<Response> getOrderById(Long orderId);

    ResponseEntity<Response> getAllOrder();
}
