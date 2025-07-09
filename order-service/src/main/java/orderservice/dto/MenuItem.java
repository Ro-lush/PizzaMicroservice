package orderservice.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MenuItem {
    private Long id;
    private String name;
    private Double price;
}

