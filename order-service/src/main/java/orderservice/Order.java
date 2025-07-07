package orderservice;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private Double orderAmount;

    private String status;
    @ElementCollection
    private List<Long> items;
}
