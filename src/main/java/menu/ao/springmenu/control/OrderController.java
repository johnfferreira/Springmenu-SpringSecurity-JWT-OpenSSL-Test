package menu.ao.springmenu.control;

import menu.ao.springmenu.dto.CreateOrderDto;
import menu.ao.springmenu.entity.Order;
import menu.ao.springmenu.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAuthority('SCOPE_admin')")
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> listAllOrder() {
        var order = this.orderService.getAllOrder();

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findAOrder(@PathVariable Long id) {
        var order = this.orderService.getOrderById(id);

        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasAuthority('SCOPE_admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        this.orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}