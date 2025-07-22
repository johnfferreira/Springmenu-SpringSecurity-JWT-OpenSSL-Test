package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateOrderDto;
import menu.ao.springmenu.entity.Order;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.exception.PersistenceException;
import menu.ao.springmenu.repository.OrderRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;

    public OrderService(UserService userService, OrderRepository orderRepository) {
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CreateOrderDto createOrderDto) {

        var userId = this.userService.getUserById(createOrderDto.userID());

        var order = new Order();

        order.setUser(userId);
        order.setStatus(createOrderDto.status());
        order.setTotalPrice(createOrderDto.totalPrice());
        order.setCreatedAt(LocalDateTime.now());

        try {
            return this.orderRepository.save(order);
        } catch (IllegalArgumentException e) {

            throw new PersistenceException("Erro a cadastrar");
        } catch (OptimisticLockingFailureException e) {

            throw new PersistenceException("Erro a cadastrar");
        }

    }


    public Order getOrderById(Long id) {

        return this.orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ordem nao encontrado"));
    }

    public List<Order> getAllOrder() {
        return this.orderRepository.findAll();
    }


    public void deleteOrder(Long id) {
        if (!this.orderRepository.existsById(id)) {
            throw new NotFoundException("Ordem não encontrado");
        }
        this.orderRepository.deleteById(id);
    }
}
