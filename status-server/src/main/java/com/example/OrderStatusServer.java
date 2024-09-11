package com.example;

import com.example.grpc.UpdatedOrderStatusRequest;
import com.example.grpc.UpdatedOrderStatusResponse;
import com.example.grpc.UpdatedOrderStatusServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class OrderStatusServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new UpdatedOrderStatusServiceImpl())
                .build();

        System.out.println("Server started on port 8080...");
        server.start();
        server.awaitTermination();
    }

    // Implementation of the gRPC service
    static class UpdatedOrderStatusServiceImpl extends UpdatedOrderStatusServiceGrpc.UpdatedOrderStatusServiceImplBase {

        @Override
        public void updatedOrderStatus(UpdatedOrderStatusRequest request, StreamObserver<UpdatedOrderStatusResponse> responseObserver) {
            String orderId = request.getOrderId();
            String userId = request.getUserId();
            UpdatedOrderStatusResponse response = UpdatedOrderStatusResponse.newBuilder()
                    .setOrderId(orderId)
                    .setUserId(userId)
                    .setStatus(request.getStatus())
                    .build();

            System.out.println("Order ID: " + orderId + ", User ID: " + userId + " updated with status: " + request.getStatus());

            // Send the response back to the client
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}

