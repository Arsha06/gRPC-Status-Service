package com.example.service;


import com.example.grpc.UpdatedOrderStatusRequest;
import com.example.grpc.UpdatedOrderStatusResponse;
import com.example.entity.Order;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public UpdatedOrderStatusResponse updateOrderStatus(UpdatedOrderStatusRequest request) {
        Order order = orderRepository.findByOrderId(request.getOrderId());
        if (order != null) {
            // Set the status from the gRPC request (which is an enum)
            order.setStatus(request.getStatus());


            // Save the updated order back to the repository
            orderRepository.save(order);

            // Build and return the gRPC response
            return UpdatedOrderStatusResponse.newBuilder()
                    .setOrderId(order.getOrderId())
                    .setUserId(order.getUserId())
                    .setStatus(order.getStatus())  // Enum returned correctly
                    .build();
        } else {
            // Handle the case where the order was not found
            System.err.println("Order not found: " + request.getOrderId());

            // Return a response with default values or throw an exception
            return UpdatedOrderStatusResponse.newBuilder()
                    .setOrderId("Not Found")
                    .setUserId("Unknown")
                    .setStatus(com.example.grpc.UpdatedOrderStatus.UNRECOGNIZED) // Use a default or error enum
                    .build();
        }
    }
}
