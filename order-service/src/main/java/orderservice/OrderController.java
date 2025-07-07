package orderservice;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderController(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public Order createOrder(@RequestBody List<Long> items) {
        // Проверяем существование всех items в меню
        for (Long itemId : items) {
            String menuServiceUrl = "http://localhost:8084/menu/" + itemId;
            restTemplate.getForObject(menuServiceUrl, Object.class);
        }

        // Отправляем заказ на кухню
        Order newOrder = new Order();
        newOrder.setItems(items);
        newOrder.setStatus("CREATED");
        orderRepository.save(newOrder);

        String kitchenServiceUrl = "http://localhost:8085/kitchen/orders";
        restTemplate.postForObject(kitchenServiceUrl, newOrder, Object.class);

        return newOrder;
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestBody String status) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        return orderRepository.save(order);
    }
}