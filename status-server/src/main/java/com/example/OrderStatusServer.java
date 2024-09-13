package com.example;

import com.example.grpc.UpdatedOrderStatus;
import com.example.grpc.UpdatedOrderStatusRequest;
import com.example.grpc.UpdatedOrderStatusResponse;
import com.example.grpc.UpdatedOrderStatusServiceGrpc;
import com.example.entity.Order;
import com.example.repository.OrderRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
public class OrderStatusServer implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run(OrderStatusServer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        startGrpcServer();
    }

    public void startGrpcServer() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9091)
                .addService(new UpdatedOrderStatusServiceImpl(orderRepository))
                .build();

        System.out.println("Server started on port 9091...");
        server.start();
        server.awaitTermination();
    }

    // gRPC Service Implementation
    @Component
    public static class UpdatedOrderStatusServiceImpl extends UpdatedOrderStatusServiceGrpc.UpdatedOrderStatusServiceImplBase {

        private final OrderRepository orderRepository;

        // Constructor Injection
        public UpdatedOrderStatusServiceImpl(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
        }

        @Override
        @Transactional
        public void updatedOrderStatus(UpdatedOrderStatusRequest request, StreamObserver<UpdatedOrderStatusResponse> responseObserver) {

            try {
                // Convert orderId and userId from String to UUID
                UUID orderId = UUID.fromString(request.getOrderId());
                UUID userId = UUID.fromString(request.getUserId());

                UpdatedOrderStatus status = request.getStatus();

            // Update the order status in the database

            Order order = orderRepository.findByOrderId(orderId);
            if (order != null) {
                order.setStatus(status); // No need for valueOf as status is an enum
                orderRepository.save(order);

                // The status is already an enum, no need to convert from string
                UpdatedOrderStatus updatedStatus = order.getStatus();

                // Build the response
                UpdatedOrderStatusResponse response = UpdatedOrderStatusResponse.newBuilder()
                        .setOrderId(order.getOrderId().toString())
                        .setUserId(order.getUserId().toString())
                        .setStatus(updatedStatus) // Convert stored string back to enum

                        .build();


                // Send the response back to the client
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                System.out.println("Order ID: " + orderId + ", User ID: " + userId + " updated with status: " + status);

            } else {
                // If order not found, respond with an error
                System.err.println("Order not found: " + orderId);
                responseObserver.onError(new RuntimeException("Order not found"));
            }
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid UUID format for orderId or userId: " + e.getMessage());
            } finally {
                responseObserver.onCompleted();
            }

        }
    }
}
