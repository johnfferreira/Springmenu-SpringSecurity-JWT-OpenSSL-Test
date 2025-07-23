package menu.ao.springmenu.service;

import menu.ao.springmenu.dto.CreateUserDto;
import menu.ao.springmenu.entity.Role;
import menu.ao.springmenu.entity.User;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.exception.UserIsPresentException;
import menu.ao.springmenu.repository.RoleRepository;
import menu.ao.springmenu.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;  // Adicionado
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<UUID> userByIdArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("create a user with sucess")
        void createUserWithSucess() {
            var basicRole = new Role();
            basicRole.setName(Role.values.BASIC.name());
            when(roleRepository.findByName(Role.values.BASIC.name()))
                    .thenReturn(Optional.of(basicRole));
            when(userRepository.findByUserName(anyString()))
                    .thenReturn(Optional.empty());

            when(passwordEncoder.encode(anyString()))
                    .thenReturn("encodedPassword");

            User expectedUser = new User();
            expectedUser.setId(UUID.randomUUID());
            expectedUser.setName("john");
            expectedUser.setUserName("john@mail.com");
            expectedUser.setPassword("encodedPassword");
            expectedUser.setRole(Set.of(basicRole));


            doReturn(expectedUser).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto("john", "john@mail.com", "123");
            userService.createUser(input);
            var userCap = userArgumentCaptor.getValue();

            assertEquals(input.userName(), userCap.getUserName());
            assertEquals(input.name(), userCap.getName());
            assertEquals("encodedPassword", userCap.getPassword());
            verify(userRepository).save(any(User.class));
            verify(passwordEncoder).encode("123");
        }

        @Test
        @DisplayName("should Throw Exception When User Exists")
        void shouldThrowWhenUserExists() {
            Role basicRole = new Role();
            basicRole.setName("BASIC");
            when(roleRepository.findByName("BASIC"))
                    .thenReturn(Optional.of(basicRole));

            when(userRepository.findByUserName("john@mail.com"))
                    .thenReturn(Optional.of(new User()));

            var input = new CreateUserDto("john", "john@mail.com", "123");
            assertThrows(UserIsPresentException.class,
                    () -> userService.createUser(input),
                    "Lançar exceção quando usuário já existe");
        }
    }

    @Nested
    class getUSerById {
        @Test
        @DisplayName("get a user with sucess")
        void getUserByIdWithSucess() {
            var basicRole = new Role();
            basicRole.setName(Role.values.BASIC.name());

            User expectedUser = new User();
            expectedUser.setId(UUID.randomUUID());

            doReturn(Optional.of(expectedUser)).when(userRepository).findById(userByIdArgumentCaptor.capture());

            var output = userService.getUserById(expectedUser.getId());

            assertEquals(output.getId(), userByIdArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("get a user with sucess when optional is empty")
        void getUserByIdWithSucessWhenOptionalIsEmpty() {

            var nonExistentId = UUID.randomUUID();
            when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> {
                userService.getUserById(nonExistentId);
            });
        }
    }

    @Nested
    class getAllUser {
        @Test
        void getAlluserWithSucess() {
            var basicRole = new Role();
            basicRole.setName(Role.values.BASIC.name());

            User expectedUser = new User();
            expectedUser.setId(UUID.randomUUID());
            expectedUser.setName("john");
            expectedUser.setUserName("john@mail.com");
            expectedUser.setPassword("encodedPassword");
            expectedUser.setRole(Set.of(basicRole));

            doReturn(List.of(expectedUser)).
                    when(userRepository).
                    findAll();

            var output = userService.getAllUsers();
            assertNotNull(output);
            assertEquals(1, output.size());
        }

        @Test
        void getAlluserPageWithSucess(){
            var basicRole = new Role();
            basicRole.setName(Role.values.BASIC.name());

            User expectedUser = new User();
            expectedUser.setId(UUID.randomUUID());
            expectedUser.setName("john");
            expectedUser.setUserName("john@mail.com");
            expectedUser.setPassword("encodedPassword");
            expectedUser.setRole(Set.of(basicRole));

            Pageable pageable = PageRequest.of(0, 2);
            List<User> userList = List.of(expectedUser);
            Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());
            doReturn(userPage).
                    when(userRepository).
                    findAll(pageable);

            Page output =  userService.getAllUsersPage(pageable);
            assertNotNull(output);
            assertEquals(1, output.getContent().size());
            assertEquals(expectedUser, output.getContent().get(0));
        }
    }

    @Nested
    class deleteById {
        @Test
        void deleteByIdWithSucessWhenUserExists() {

            doReturn(true)
                    .when(userRepository)
                    .existsById(userByIdArgumentCaptor.capture());

            doNothing()
                    .when(userRepository)
                    .deleteById(userByIdArgumentCaptor.capture());

            var userId = UUID.randomUUID();
            userService.deleteUser(userId);
            var allId = userByIdArgumentCaptor.getAllValues();
            assertEquals(userId, allId.get(0));
            assertEquals(userId, allId.get(1));
        }

        @Test
        void dontDeleteByIdWithSucessWhenUserDontExists() {

            var nonExistentId = UUID.randomUUID();
            assertThrows(NotFoundException.class, () -> userService.deleteUser(nonExistentId));

            verify(userRepository, never()).delete(any());
        }
    }

}