package com.example.task_tracker.service;
import com.example.task_tracker.entity.Task;
import com.example.task_tracker.repository.TaskRepository;
import com.example.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public Flux<Task> findAll() {
        return taskRepository.findAll()
                .flatMap(task -> Mono.zip(
                        Mono.just(task),
                        userRepository.findById(task.getAuthorId()),
                        userRepository.findById(task.getAssigneeId()),
                        Flux.fromIterable(task.getObserverIds()).flatMap(userRepository::findById).collectList()
                ).map(tuple -> {
                    Task task1 = new Task();
                    task1.setId(tuple.getT1().getId());
                    task1.setName(tuple.getT1().getName());
                    task1.setDescription(tuple.getT1().getDescription());
                    task1.setCreatedAt(tuple.getT1().getCreatedAt());
                    task1.setUpdatedAt(tuple.getT1().getUpdatedAt());
                    task1.setStatus(tuple.getT1().getStatus());
                    task1.setAuthorId(tuple.getT1().getAuthorId());
                    task1.setAssigneeId(tuple.getT1().getAssigneeId());
                    task1.setObserverIds(tuple.getT1().getObserverIds());

                    task1.setAuthor(tuple.getT2());
                    task1.setAssignee(tuple.getT3());
                    task1.setObservers(tuple.getT4().stream().collect(Collectors.toSet()));
                    return task1;
                }));
    }

    public Mono<Task> findById(String id) {
        return taskRepository.findById(id)
                .flatMap(task -> Mono.zip(
                        Mono.just(task),
                        userRepository.findById(task.getAuthorId()),
                        userRepository.findById(task.getAssigneeId()),
                        Flux.fromIterable(task.getObserverIds()).flatMap(userRepository::findById).collectList()
                ).map(tuple -> {
                    Task task1 = new Task();
                    task1.setId(tuple.getT1().getId());
                    task1.setName(tuple.getT1().getName());
                    task1.setDescription(tuple.getT1().getDescription());
                    task1.setCreatedAt(tuple.getT1().getCreatedAt());
                    task1.setUpdatedAt(tuple.getT1().getUpdatedAt());
                    task1.setStatus(tuple.getT1().getStatus());
                    task1.setAuthorId(tuple.getT1().getAuthorId());
                    task1.setAssigneeId(tuple.getT1().getAssigneeId());
                    task1.setObserverIds(tuple.getT1().getObserverIds());

                    task1.setAuthor(tuple.getT2());
                    task1.setAssignee(tuple.getT3());
                    task1.setObservers(tuple.getT4().stream().collect(Collectors.toSet()));
                    return task1;
                }));
    }

    public Mono<Task> create(Task task) {
        return taskRepository.save(task);
    }

    public Mono<Task> update (String id, Task task){
        return findById(id).flatMap(taskForUpdate -> {
            if (StringUtils.hasText(task.getName())) {
                taskForUpdate.setName(task.getName());
            }
            if (StringUtils.hasText(task.getDescription())) {
                taskForUpdate.setDescription(task.getDescription());
            }
            if (task.getCreatedAt() != null) {
                taskForUpdate.setCreatedAt(task.getCreatedAt());
            }
            if (task.getUpdatedAt() != null) {
                taskForUpdate.setUpdatedAt(task.getUpdatedAt());
            }
            if (task.getStatus() != null) {
                taskForUpdate.setStatus(task.getStatus());
            }
            if (StringUtils.hasText(task.getAuthorId())) {
                taskForUpdate.setAuthorId(task.getAuthorId());
            }
            if (StringUtils.hasText(task.getAssigneeId())) {
                taskForUpdate.setAssigneeId(task.getAssigneeId());
            }
            if (task.getObserverIds().size() != 0) {
                taskForUpdate.setObserverIds(task.getObserverIds());
            }
                return taskRepository.save(taskForUpdate);
            });
        }

    public Mono<Task> addObserver(String id, String observerId){
        return findById(id).flatMap(taskForUpdate -> {
            if (taskForUpdate.getObserverIds().size() != 0) {
                taskForUpdate.getObserverIds().add(observerId);
            }
            return taskRepository.save(taskForUpdate);
        });
    }

    public Mono<Void> deleteById(String id){
        return taskRepository.deleteById(id);
    }
}
