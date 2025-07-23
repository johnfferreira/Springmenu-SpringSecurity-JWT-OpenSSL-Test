package menu.ao.springmenu.control;

import menu.ao.springmenu.entity.Order;
import menu.ao.springmenu.entity.OrderItem;
import menu.ao.springmenu.service.OrderItemService;
import menu.ao.springmenu.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/orderItem")
public class OrderItemController {


    private final OrderItemService orderItemService;
    private final OrderService orderService;

    public OrderItemController(OrderItemService orderItemService, OrderService orderService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> listAllOrderItem(JwtAuthenticationToken auth, Pageable pageable) {
        var acess = UUID.fromString(auth.getName());
        var orderItem = this.orderItemService.getAllOrderItemPage(acess, pageable);

        return ResponseEntity.ok(orderItem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> findAOrderItem(@PathVariable Long id, JwtAuthenticationToken acess) {

        var userAcess = UUID.fromString(acess.getName());
        var orderItem = this.orderItemService.getOrderItemById(userAcess, id);

        return ResponseEntity.ok(orderItem);
    }

   // @PreAuthorize("hasAuthority('SCOPE_admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id, JwtAuthenticationToken acess) {

        var userAcess = UUID.fromString(acess.getName());
        this.orderItemService.deleteOrderItem(userAcess,id);
        return ResponseEntity.ok().build();
    }
}
