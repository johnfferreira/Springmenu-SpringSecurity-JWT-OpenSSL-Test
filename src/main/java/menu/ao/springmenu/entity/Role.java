package menu.ao.springmenu.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum values {

        ADMIN(1L),
        BASIC(2L);

        long rolesId;

        values(long rolesId) {
            this.rolesId = rolesId;
        }

        public long getRolesId() {
            return rolesId;
        }
    }
}