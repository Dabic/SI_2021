package rs.raf.broker.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.broker.domain.Team;
import rs.raf.broker.domain.User;
import rs.raf.broker.dtos.UserDTO;
import rs.raf.broker.exceptions.CustomHttpException;
import rs.raf.broker.mappers.UserMapper;
import rs.raf.broker.persistance.RoleRepository;
import rs.raf.broker.persistance.TeamRepository;
import rs.raf.broker.persistance.UserRepository;
import rs.raf.broker.services.UserAdministrationService;

import java.util.List;

@Service
public class UserAdministrationServiceImpl implements UserAdministrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TeamRepository teamRepository;

    public UserAdministrationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.teamRepository = teamRepository;
    }

    @Override
    public User save(UserDTO userDTO) {
        ifRolesNotFoundThrowException(userDTO.getRoles());
        User user = userMapper.toUser(userDTO);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        ifUserExistsThrowException(user);
        return userRepository.save(user);
    }

    @Override
    public User update(UserDTO userDTO) {
        User userToUpdate = findUserByUsername(userDTO.getUsername());
        User mappedUser = userMapper.toUser(userDTO);
        userToUpdate.setPassword(bCryptPasswordEncoder.encode(mappedUser.getPassword()));
        userToUpdate.setRoles(mappedUser.getRoles());
        userToUpdate.setTeam(userDTO.getTeam());
        return userRepository.save(userToUpdate);
    }

    @Override
    public void delete(UserDTO userDTO) {
        User user = findUserByUsername(userDTO.getUsername());
        userRepository.delete(user);
    }

    public void ifRolesNotFoundThrowException(List<String> roles) {
        roles.forEach(role -> roleRepository.findById(role)
                .orElseThrow(() -> new CustomHttpException(
                        String.format("Role %s not found!", role),
                        HttpStatus.NOT_FOUND)));
    }

    public void ifUserExistsThrowException(User user) {
        if (userRepository.findById(user.getUsername()).isPresent()) {
            throw new CustomHttpException(
                    String.format("User %s already exists!", user.getUsername()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findById(username).orElseThrow(() ->
                new CustomHttpException(
                        String.format("User %s not found!", username),
                        HttpStatus.NOT_FOUND));
    }
}
