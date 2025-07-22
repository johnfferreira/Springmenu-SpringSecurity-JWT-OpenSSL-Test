package menu.ao.springmenu.control;

import jakarta.validation.Valid;
import menu.ao.springmenu.dto.APICartRequestDto;
import menu.ao.springmenu.dto.CreateOrderDto;
import menu.ao.springmenu.dto.CreateOrderItemDto;
import menu.ao.springmenu.service.OrderItemService;
import menu.ao.springmenu.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public CartController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> processCart(@Valid @RequestBody APICartRequestDto apiCartRequestDto, JwtAuthenticationToken authToken) {

        var userId = UUID.fromString(authToken.getName());

        var createOrderDto = new CreateOrderDto(userId, "pendente", apiCartRequestDto.totalPrice());
        var orderFromDb = this.orderService.createOrder(createOrderDto);

        List<CreateOrderItemDto> items = apiCartRequestDto.items().stream()
                .map(cart ->
                        new CreateOrderItemDto(orderFromDb.getId(), cart.dishId(), cart.quantity(), cart.price()))
                .toList();

        this.orderItemService.createOrderItems(items);

        return ResponseEntity.ok().build();
    }

}
