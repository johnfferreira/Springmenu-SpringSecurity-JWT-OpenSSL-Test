package menu.ao.springmenu.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderDto(

        @NotNull
        UUID userID,
        String status,
        @NotNull
        Double totalPrice
) {
}
