package menu.ao.springmenu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record APICartRequestDto
        (
                @NotNull
                List<CartRequestDto> items,
                @NotNull
                Double totalPrice
        ) {
}
