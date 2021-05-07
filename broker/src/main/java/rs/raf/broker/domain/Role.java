package rs.raf.broker.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USER_ROLE")
public class Role implements GrantedAuthority {

    @Id
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ManyToMany(mappedBy = "roles")
    private List<Endpoint> endpoints;

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
