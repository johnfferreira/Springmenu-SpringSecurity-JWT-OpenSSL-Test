package menu.ao.springmenu.control;

import jakarta.validation.Valid;
import menu.ao.springmenu.dto.CreateUserDto;
import menu.ao.springmenu.entity.User;
import menu.ao.springmenu.repository.UserRepository;
import menu.ao.springmenu.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> newUser(@Valid @RequestBody CreateUserDto userDto) {

        this.userService.createUser(userDto);

        return ResponseEntity.ok().build();

    }

    @GetMapping
    // @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> listAllUser(Pageable pageable) {
        var users = this.userService.getAllUsersPage(pageable).getContent();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<User> findAUser(@PathVariable UUID id) {
        var users = this.userService.getUserById(id);

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }


}