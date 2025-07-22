package menu.ao.springmenu.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String userName,
        @NotBlank
        String password ) {
}
