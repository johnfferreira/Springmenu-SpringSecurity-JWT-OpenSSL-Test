package menu.ao.springmenu.repository;


import menu.ao.springmenu.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> { }