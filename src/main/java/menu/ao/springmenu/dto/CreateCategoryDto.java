package menu.ao.springmenu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryDto(
        @NotBlank
        String name,
        String description)
{

}
