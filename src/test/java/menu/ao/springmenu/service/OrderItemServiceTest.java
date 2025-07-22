package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateOrderItemDto;
import menu.ao.springmenu.entity.*;
import menu.ao.springmenu.repository.OrderItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @InjectMocks
    private OrderItemService orderItemService;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private DishService dishService;
    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<List<OrderItem>>  orderItemListCaptor;
    @Captor
    private ArgumentCaptor<Long> oderItemByIdArgumentCaptor;

    @Nested
    class createOrderItem {

        @Test
        @DisplayName("Create order items with success")
        void createOrderItemWithSucess() {

            Order mockOrder = new Order();
            Dish mockDish = new Dish();
            mockOrder.setId(1L);
            mockDish.setId(1L);

            when(orderService.getOrderById(1L)).thenReturn(mockOrder);
            when(dishService.getDishsByIds(List.of(1L))).thenReturn(List.of(mockDish));

            when(orderItemRepository.saveAll(anyList()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            var input = new CreateOrderItemDto(1L, 1L, 2, 20D);

            orderItemService.createOrderItems(List.of(input));

            verify(orderItemRepository).saveAll(orderItemListCaptor.capture());

            List<OrderItem> savedItems = orderItemListCaptor.getValue();

            assertEquals(1, savedItems.size());

            OrderItem savedItem = savedItems.get(0);
            assertEquals(mockDish, savedItem.getDish());
            assertEquals(mockOrder, savedItem.getOrder());
            assertEquals(2, savedItem.getQuantity());
            assertEquals(20D, savedItem.getSubtotal(), 0.001);
        }
    }

    @Nested
    class getOrderItemById {
        @Test
        @DisplayName("Get order item by ID with success")
        void getOrderItemByIdWithSuccess() {

            UUID userAccess = UUID.randomUUID();
            Long orderItemId = 1L;
            OrderItem mockOrderItem = new OrderItem();
            mockOrderItem.setId(orderItemId);

            User mockUser = new User();
            mockUser.setId(userAccess);
            Order mockOrder = new Order();
            mockOrder.setUser(mockUser);
            mockOrderItem.setOrder(mockOrder);

            when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(mockOrderItem));

            OrderItem result = orderItemService.getOrderItemById(userAccess, orderItemId);

            assertNotNull(result);
            assertEquals(orderItemId, result.getId());
            verify(orderItemRepository).findById(orderItemId);
        }
        // MAke for dont sucess
    }

    @Nested
    class getAllOrderItem{
        @Test
        @DisplayName("Get all order items - Regular user")
        void getAllOrderItemsRegularUser() {

            UUID userAccess = UUID.randomUUID();
            UUID otherUserAccess = UUID.randomUUID();

            OrderItem userItem = new OrderItem();
            Order userOrder = new Order();
            User user =  new User();
            user.setId(userAccess);
            userOrder.setUser(user);
            userItem.setOrder(userOrder);

            OrderItem otherItem = new OrderItem();
            Order otherOrder = new Order();
            User otherUser =  new User();
            otherUser.setId(otherUserAccess);
            otherOrder.setUser(otherUser);
            otherItem.setOrder(otherOrder);

            List<OrderItem> allItems = Arrays.asList(userItem, otherItem);

            // Mockar verificação de admin
           when(userService.isAdmin(userAccess)).thenReturn(false);
            when(orderItemRepository.findAll()).thenReturn(allItems);

            // Act
            List<OrderItem> result = orderItemService.getAllOrderItem(userAccess);

            // Assert
            assertEquals(1, result.size());
            assertTrue(result.contains(userItem));
            verify(orderItemRepository).findAll();
            verify(userService).isAdmin(userAccess);
        }

        @Test
        @DisplayName("Get all order items - Admin user")
        void getAllOrderItemsAdmin() {

            UUID adminAccess = UUID.randomUUID();
            List<OrderItem> allItems = Arrays.asList(
                    new OrderItem(),
                    new OrderItem()
            );

            when(userService.isAdmin(adminAccess)).thenReturn(true);
            when(orderItemRepository.findAll()).thenReturn(allItems);

            List<OrderItem> result = orderItemService.getAllOrderItem(adminAccess);

            assertEquals(2, result.size());
            verify(orderItemRepository).findAll();
            verify(userService).isAdmin(adminAccess);
        }
    }

    @Nested
    class deleteOrderItem{
        @Test
        @DisplayName("Delete order item with success")
        void deleteOrderItemWithSuccess() {

            UUID userAccess = UUID.randomUUID();
            Long orderItemId = 1L;
            OrderItem mockOrderItem = new OrderItem();
            mockOrderItem.setId(orderItemId);

            User mockUser = new User();
            mockUser.setId(userAccess);
            Order mockOrder = new Order();
            mockOrder.setUser(mockUser);
            mockOrderItem.setOrder(mockOrder);

            when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(mockOrderItem));
            doNothing().when(orderItemRepository).deleteById(orderItemId);

            assertDoesNotThrow(() ->
                    orderItemService.deleteOrderItem(userAccess, orderItemId)
            );

            verify(orderItemRepository).findById(orderItemId);
            verify(orderItemRepository).deleteById(orderItemId);
        }

        // MAke for dont Sucess
    }
}