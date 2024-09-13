package com.example.entity;
import java.util.UUID;
import com.example.grpc.UpdatedOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING) // Ensure status is stored as a string
    private UpdatedOrderStatus status = UpdatedOrderStatus.PLACED; // Default status



}

