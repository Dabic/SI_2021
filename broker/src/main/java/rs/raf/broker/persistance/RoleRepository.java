package rs.raf.broker.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.broker.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
