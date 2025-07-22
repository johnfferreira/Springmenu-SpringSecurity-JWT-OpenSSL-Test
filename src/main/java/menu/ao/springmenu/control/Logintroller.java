package menu.ao.springmenu.control;

import jakarta.validation.Valid;
import menu.ao.springmenu.dto.LoginRequest;
import menu.ao.springmenu.dto.LoginResponse;
import menu.ao.springmenu.repository.UserRepository;
import menu.ao.springmenu.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Logintroller {

    private final TokenService tokenService;

    public Logintroller(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginResponse(@Valid @RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(this.tokenService.myTokenResponse(loginRequest));

    }
}
