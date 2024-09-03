package com.example.task_tracker.controller;
import com.example.task_tracker.DTO.UserDto;
import com.example.task_tracker.mapper.UserMapper;
import com.example.task_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserDto> findAllUsers() {
        return userService.findAll().map(userMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<UserDto> findUserById(@PathVariable String id) {
        return userService.findById(id).map(userMapper::toDto);
    }

    @PostMapping
    public Mono<UserDto> createUser(@RequestBody UserDto userDto) {
        return userService.create(userMapper.toEntity(userDto)).map(userMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return userService.update(id, userMapper.toEntity(userDto)).map(userMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable String id) {
        return userService.deleteById(id);
    }

}
