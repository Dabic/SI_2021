package rs.raf.broker.mappers;

import org.springframework.stereotype.Service;
import rs.raf.broker.domain.User;
import rs.raf.broker.dtos.UserDTO;

@Service
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUserDto(User user);
}
