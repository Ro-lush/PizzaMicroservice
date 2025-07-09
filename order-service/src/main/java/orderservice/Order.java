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

    private LocalDateTime createdAt;
    private Double orderAmount;

    private String status;
    @ElementCollection
    private List<Long> items;
}
