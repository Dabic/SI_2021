package rs.raf.broker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.broker.dtos.UserDTO;
import rs.raf.broker.mappers.UserMapper;
import rs.raf.broker.services.UserAdministrationService;

@RestController
@RequestMapping("/api/users")
public class UserAdministrationController {

    private final UserAdministrationService userAdministrationService;
    private final UserMapper userMapper;

    public UserAdministrationController(UserAdministrationService userAdministrationService, UserMapper userMapper) {
        this.userAdministrationService = userAdministrationService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO user) {
        return new ResponseEntity<>(userMapper.toUserDto(userAdministrationService.save(user)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO user) {
        return new ResponseEntity<>(userMapper.toUserDto(userAdministrationService.update(user)), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<UserDTO> delete(@RequestBody UserDTO user) {
        userAdministrationService.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
