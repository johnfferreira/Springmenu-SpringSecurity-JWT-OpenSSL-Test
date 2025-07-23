package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateUserDto;
import menu.ao.springmenu.entity.Role;
import menu.ao.springmenu.entity.User;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.exception.PersistenceException;
import menu.ao.springmenu.exception.UserIsPresentException;
import menu.ao.springmenu.repository.RoleRepository;
import menu.ao.springmenu.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public void createUser(CreateUserDto userDto) {

        var basicRole = this.roleRepository.findByName(Role.values.BASIC.name())
                .orElseThrow(() -> new NotFoundException("Role BASIC nao encontrada"));
        var userFromDb = this.userRepository.findByUserName(userDto.userName());

        if (userFromDb.isPresent()) {

            throw new UserIsPresentException("Usuario ja existe");
        }

        var user = new User();

        user.setName(userDto.name());
        user.setUserName(userDto.userName());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(Set.of(basicRole));
        user.setCreatedAt(LocalDateTime.now());
        try {
            userRepository.save(user);
        } catch (IllegalArgumentException e) {

            throw new PersistenceException(e.getMessage());
        } catch (OptimisticLockingFailureException e) {

            throw new PersistenceException("Erro a cadastrar");
        }

    }

    public User getUserById(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public Page<User> getAllUsersPage(Pageable page) {
        return this.userRepository.findAll(page);
    }

    public User updateUser(UUID id, CreateUserDto updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.name());
                    user.setUserName(updatedUser.userName());
                    // user.setRole(updatedUser.getRole());
                    user.setCreatedAt(LocalDateTime.now());

                    if (updatedUser.password() != null && !updatedUser.password().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(updatedUser.password()));
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public void deleteUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado");
        }

        this.userRepository.deleteById(id);
    }

    public boolean isAdmin(UUID userAcess) {
        var admin = this.getUserById(userAcess);
        return admin.getRole().stream()
                .anyMatch(aRole -> aRole.getId() == Role.values.ADMIN.getRolesId());
    }

}
