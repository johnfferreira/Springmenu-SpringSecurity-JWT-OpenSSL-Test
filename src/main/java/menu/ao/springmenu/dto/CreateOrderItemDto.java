package menu.ao.springmenu.dto;


import jakarta.validation.constraints.NotNull;

public record CreateOrderItemDto(

        @NotNull
        Long orderId,
        @NotNull
        Long dishId,
        @NotNull
        Integer quantity,
        @NotNull
        Double subtotal
) {
}
