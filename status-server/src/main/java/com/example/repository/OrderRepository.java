package com.example.repository;


import java.util.UUID;
import com.example.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(UUID orderId);
}
