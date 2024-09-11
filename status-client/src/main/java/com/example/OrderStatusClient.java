package com.example;


import com.example.grpc.UpdatedOrderStatus;
import com.example.grpc.UpdatedOrderStatusRequest;
import com.example.grpc.UpdatedOrderStatusResponse;
import com.example.grpc.UpdatedOrderStatusServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;



public class OrderStatusClient {

    public static void main(String[] args) {
        // Create a channel to connect to the server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        // Create a stub for the service
        UpdatedOrderStatusServiceGrpc.UpdatedOrderStatusServiceBlockingStub stub =
                UpdatedOrderStatusServiceGrpc.newBlockingStub(channel);

        // Build the request
        UpdatedOrderStatusRequest request = UpdatedOrderStatusRequest.newBuilder()
                .setOrderId("ORD123")
                .setUserId("USER456")
                .setStatus(com.example.grpc.UpdatedOrderStatus.PLACED)
                .build();

        // Call the gRPC service
        UpdatedOrderStatusResponse response = stub.updatedOrderStatus(request);

        // Print the response
        System.out.println("Response from server: Order ID = " + response.getOrderId() +
                ", User ID = " + response.getUserId() +
                ", Status = " + response.getStatus());

        // Shutdown the channel
        channel.shutdown();
    }
}
