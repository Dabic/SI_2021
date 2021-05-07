package rs.raf.broker.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.broker.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
