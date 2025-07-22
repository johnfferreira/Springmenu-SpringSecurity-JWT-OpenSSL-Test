package menu.ao.springmenu.config;

import menu.ao.springmenu.entity.Role;
import menu.ao.springmenu.entity.User;
import menu.ao.springmenu.exception.NotFoundException;
import menu.ao.springmenu.repository.RoleRepository;
import menu.ao.springmenu.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

     private final RoleRepository roleRepository;
     private final UserRepository userRepository;
     private final  BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = this.roleRepository.findByName(Role.values.ADMIN.name())
                .orElseThrow(() -> new NotFoundException("Role ADMIN nao encontrada"));
        var userAdmin = this.userRepository.findByUserName("admin");
        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Usuario ja existe");
                },
                ()->{
                    var user = new User();
                    user.setName("admin");
                    user.setUserName("admin");
                    user.setPassword(bCryptPasswordEncoder.encode("123"));
                    user.setRole(Set.of(roleAdmin));
                    this.userRepository.save(user);
                }
        );
    }
}
