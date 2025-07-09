package orderservice;



import orderservice.dto.MenuItem;
import orderservice.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // Проверка меню и расчет суммы
            double totalAmount = 0.0;
            for (Long itemId : orderRequest.getItems()) {
                String url = menuServiceUrl + "/api/" + itemId;
                ResponseEntity<MenuItem> response = restTemplate.getForEntity(url, MenuItem.class);

                if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                    return ResponseEntity.badRequest().body("Пункт меню с id " + itemId + " не найден");
                }

                totalAmount += response.getBody().getPrice();
            }

            Order order = new Order();
            order.setCustomerName(orderRequest.getCustomerName());
            order.setCreatedAt(LocalDateTime.now());
            order.setOrderAmount(totalAmount);
            order.setStatus("CREATED");
            order.setItems(orderRequest.getItems());

            Order savedOrder = orderRepository.save(order);

            // Отправка заказа на кухню
            restTemplate.postForEntity(kitchenServiceUrl, savedOrder, Void.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка в создании заказа: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @GetMapping("/all")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
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