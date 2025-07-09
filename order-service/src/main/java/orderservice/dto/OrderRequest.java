package orderservice.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class OrderRequest {
    private String customerName;
    private List<Long> items;
}

