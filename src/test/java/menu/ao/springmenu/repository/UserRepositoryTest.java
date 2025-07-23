package menu.ao.springmenu.repository;

import menu.ao.springmenu.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("find userName with sucess")
    void findByUserNameWithSucess() {

        User user = new User();
        user.setName("johnf");
        user.setUserName("johnfferreira@gmail.com");
        entityManager.persist(user);
        entityManager.flush();

        var found = userRepository.findByUserName("johnfferreira@gmail.com");
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUserName()).isEqualTo("johnfferreira@gmail.com");
    }

    @Test
    @DisplayName("when Find By Non Existing UserName")
    public void whenFindByNonExistingUserName() {
        var found = userRepository.findByUserName("non_existing");
        assertThat(found).isEmpty();
    }

}