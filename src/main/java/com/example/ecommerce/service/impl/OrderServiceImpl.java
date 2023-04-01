package com.example.ecommerce.service.impl;

import com.example.ecommerce.domain.Order;


import com.example.ecommerce.dto.request.order.UpdateOrderRequest;
import com.example.ecommerce.dto.response.Response;
import com.example.ecommerce.exception.NotFoundException;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.service.OrderService;
import com.example.ecommerce.service.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    private final OrderRepository orderRepository;

//    @Override
//    public ResponseEntity<Response> createOrder(AddToCartRequest request) {
//
//        List<OrderItem> orderItems = new ArrayList<>();
//        for (AddToCartRequest.OrderItemDTO item : request.getItems()) {
//            OrderItem orderItem = OrderItem.builder()
//                    .product(productService.findProductById(item.getProductId()))
//                    .quantity(item.getQuantity())
//                    .build();
//            orderItems.add(orderItem);
//        }
//
//
//        Order order = Order.builder()
//                .items(orderItems)
//                .status(Order.OrderStatus.PENDING)
//                .build();
//
//        orderRepository.save(order);
//        return ResponseEntity.ok(Response.builder()
//                .status(200)
//                .message("Create order successfully")
//                .data(null)
//                .build());
//    }



//    @Override
//    public ResponseEntity<Response> deleteOrderById(Long orderId) {
//        Order order = findOrderById(orderId);
//        orderRepository.delete(order);
//
//        return ResponseEntity.ok(Response.builder()
//                .status(200)
//                .message("Delete order successfully")
//                .data(null)
//                .build());
//
//    }
    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public ResponseEntity<Response> updateOrder(UpdateOrderRequest request) {
        Order order = findOrderById(request.getOrderId());
        Order.OrderStatus status = Order.OrderStatus.valueOf(request.getStatus().toString().toUpperCase());
        order.setStatus(status);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Update order successfully")
                .data(order)
                .build());
    }




    @Override
    public ResponseEntity<Response> getOrderById(Long orderId) {
        Order order = findOrderById(orderId);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get order successfully")
                .data(order)
                .build());
    }

    @Override
    public ResponseEntity<Response> getAllOrder() {
        List<Order> allOrder = orderRepository.findAll();
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Get all order successfully")
                .data(allOrder)
                .build());
    }

    @Override
    public void save(Order order) {
        String uuid = generateRandomString(6);
        order.setOrderCode(uuid);
        orderRepository.save(order);
    }



    private static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }






}
