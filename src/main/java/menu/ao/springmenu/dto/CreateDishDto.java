package menu.ao.springmenu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDishDto(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        Double price,
        @NotBlank
        String imageUrl,
        @NotNull
        Long categoryId)
{ }
