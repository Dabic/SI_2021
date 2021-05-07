package rs.raf.broker.services;

import org.springframework.stereotype.Service;
import rs.raf.broker.domain.User;
import rs.raf.broker.dtos.UserDTO;

@Service
public interface UserAdministrationService {

    User save(UserDTO userDTO);
    User update(UserDTO userDTO);
    void delete(UserDTO userDTO);
}
