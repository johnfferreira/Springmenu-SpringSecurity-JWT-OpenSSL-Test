package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateOrderItemDto;
import menu.ao.springmenu.entity.Dish;
import menu.ao.springmenu.entity.Order;
import menu.ao.springmenu.entity.OrderItem;
import menu.ao.springmenu.entity.Role;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.exception.PersistenceException;
import menu.ao.springmenu.repository.OrderItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderItemService {


    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;
    private final DishService dishService;
    private final UserService userService;

    public OrderItemService(OrderService orderService, OrderItemRepository orderItemRepository, DishService dishService, UserService userService) {
        this.orderService = orderService;
        this.orderItemRepository = orderItemRepository;
        this.dishService = dishService;
        this.userService = userService;
    }

    @Transactional
    public void createOrderItems(List<CreateOrderItemDto> itemDtos) {
        if (itemDtos.isEmpty()) return;

        var order = orderService.getOrderById(itemDtos.get(0).orderId());

        List<Long> dishIds = itemDtos.stream().map(CreateOrderItemDto::dishId).toList();
        Map<Long, Dish> dishMap = dishService.getDishsByIds(dishIds).stream()
                .collect(Collectors.toMap(Dish::getId, d -> d));

        var orderItems = addInList(itemDtos, dishMap, order);

        try {
            this.orderItemRepository.saveAll(orderItems);
        } catch (IllegalArgumentException e) {
            throw new PersistenceException("Erro ao salvar itens do pedido");
        }
    }

    public OrderItem getOrderItemById(UUID userAcess, Long id) {

        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item não encontrado"));

        if (!hasAuthorization2(userAcess, item)) {
            throw new NotFoundException("Acesso negado");
        }
        return item;
    }

    public List<OrderItem> getAllOrderItem(UUID acess) {

        if (this.userService.isAdmin(acess))
            return this.orderItemRepository.findAll();
        return this.orderItemRepository.findAll().stream()
                .filter(item -> item.getOrder().getUser().getId().equals(acess)).toList();
    }

    public List<OrderItem> getAllOrderItemPage(UUID acess, Pageable pageable) {

        if (this.userService.isAdmin(acess))
            return this.orderItemRepository.findAll(pageable).getContent();
        return this.orderItemRepository.findAll(pageable).getContent().stream()
                .filter(item -> item.getOrder().getUser().getId().equals(acess)).toList();
    }

    public void deleteOrderItem(UUID userAcess, Long id) {

        this.getOrderItemById(userAcess, id);
        this.orderItemRepository.deleteById(id);
//        if (!hasAuthorization(userAcess, id))
//            throw new NotFoundException(" Item não encontrado");
//
    }

    private List<OrderItem> addInList(List<CreateOrderItemDto> itemDtos, Map<Long, Dish> dishMap, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (var dto : itemDtos) {
            Dish dish = dishMap.get(dto.dishId());

            if (dish == null) {
                throw new NotFoundException("Dish não encontrado: " + dto.dishId());
            }

            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setDish(dish);
            orderItem.setQuantity(dto.quantity());
            orderItem.setSubtotal(dto.subtotal());

            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private boolean hasAuthorization(UUID userAcess, Long itemId) {

        var items = findOrderItemById(itemId);
        var userOfItem = items.getOrder().getUser();
        var userId = userOfItem.getId();

        return (userId.equals(userAcess) || this.userService.isAdmin(userAcess));
    }

    private boolean hasAuthorization2(UUID userAccess, OrderItem item) {
        return (item.getOrder().getUser().getId().equals(userAccess) || this.userService.isAdmin(userAccess));
    }


    private OrderItem findOrderItemById(Long id) {

        return this.orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item nao encontrado"));
    }
}
