package com.example.task_tracker.controller;
import com.example.task_tracker.DTO.TaskDto;
import com.example.task_tracker.mapper.TaskMapper;
import com.example.task_tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public Flux<TaskDto> findAllTasks() {
        return taskService.findAll().map(taskMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<TaskDto> findTaskById(@PathVariable String id) {
        return taskService.findById(id).map(taskMapper::toDto);
    }

    @PostMapping
    public Mono<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        return taskService.create(taskMapper.toEntity(taskDto)).map(taskMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<TaskDto> updateTask(@PathVariable String id, @RequestBody TaskDto taskDto) {
        return taskService.update(id, taskMapper.toEntity(taskDto)).map(taskMapper::toDto);
    }

    @PutMapping("/{id}/addObserver")
    public Mono<TaskDto> addObserver(@PathVariable String id, @RequestBody String observerId) {
        return taskService.addObserver(id, observerId).map(taskMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id);
    }
}
