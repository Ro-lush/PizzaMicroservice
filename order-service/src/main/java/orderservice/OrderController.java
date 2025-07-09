package orderservice;



import orderservice.dto.MenuItem;
import orderservice.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${menu.service.url}")
    private String menuServiceUrl;

    @Value("${kitchen.service.url}")
    private String kitchenServiceUrl;

    public OrderController(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        // Проверка меню и расчет суммы
        double totalAmount = 0.0;
        for (Long itemId : orderRequest.getItems()) {
            String url = menuServiceUrl + "/" + itemId;
            MenuItem menuItem = restTemplate.getForObject(url, MenuItem.class);
            if (menuItem != null) {
                totalAmount += menuItem.getPrice();
            }
        }

        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderAmount(totalAmount);
        order.setStatus("CREATED");
        order.setItems(orderRequest.getItems());

        Order savedOrder = orderRepository.save(order);

        // Отправка заказа на кухню
        restTemplate.postForObject(kitchenServiceUrl, savedOrder, Void.class);

        return ResponseEntity.status(201).body(savedOrder);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestBody String status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            return ResponseEntity.ok("Статус обновлён");
        }
        return ResponseEntity.notFound().build();
    }
}