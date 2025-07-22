package menu.ao.springmenu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto (
        @NotBlank
        String name,

        @NotBlank
        @Email
        String userName,

        @NotBlank
        String password) {
}
