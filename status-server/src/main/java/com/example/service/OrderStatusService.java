package com.example.service;

import com.example.grpc.UpdatedOrderStatusRequest;
import com.example.grpc.UpdatedOrderStatusResponse;
import com.example.grpc.UpdatedOrderStatusServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService extends UpdatedOrderStatusServiceGrpc.UpdatedOrderStatusServiceImplBase {

    @Autowired
    private OrderService orderService;

    @Override
    public void updatedOrderStatus(UpdatedOrderStatusRequest request, StreamObserver<UpdatedOrderStatusResponse> responseObserver) {
        UpdatedOrderStatusResponse response = orderService.updateOrderStatus(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
