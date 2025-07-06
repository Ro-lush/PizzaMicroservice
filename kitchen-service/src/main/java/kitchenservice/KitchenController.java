package kitchenservice;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kitchen/orders")
public class KitchenController {
    private final KitchenRepository kitchenOrderRepository;

    public KitchenController(KitchenRepository kitchenOrderRepository) {
        this.kitchenOrderRepository = kitchenOrderRepository;
    }

    @PostMapping
    public Kitchen createKitchenOrder(@RequestBody Kitchen order) {
        order.setStatus("RECEIVED");
        return kitchenOrderRepository.save(order);
    }
}