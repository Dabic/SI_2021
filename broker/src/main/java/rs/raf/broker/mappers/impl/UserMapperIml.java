package rs.raf.broker.mappers.impl;

import org.springframework.stereotype.Service;
import rs.raf.broker.domain.Role;
import rs.raf.broker.domain.User;
import rs.raf.broker.dtos.UserDTO;
import rs.raf.broker.mappers.UserMapper;
import rs.raf.broker.persistance.RoleRepository;
import java.util.stream.Collectors;

@Service
public class UserMapperIml implements UserMapper {

    private final RoleRepository roleRepository;

    public UserMapperIml(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public User toUser(UserDTO userDTO) {

        return new User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                roleRepository.findAllById(userDTO.getRoles()),
                userDTO.getTeam()
        );
    }

    @Override
    public UserDTO toUserDto(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                user.getTeam());
    }
}
