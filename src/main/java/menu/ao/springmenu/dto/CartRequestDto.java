package menu.ao.springmenu.dto;

import jakarta.validation.constraints.NotNull;

public record CartRequestDto(
        @NotNull
         Long dishId,
        @NotNull
         Integer quantity,
        @NotNull
         Double price
) { }
