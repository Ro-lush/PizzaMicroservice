package menuservice;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "menu_items")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
}
