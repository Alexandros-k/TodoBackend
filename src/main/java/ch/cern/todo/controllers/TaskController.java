package ch.cern.todo.controllers;

import ch.cern.todo.models.Task;
import ch.cern.todo.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("")
    ResponseEntity<List<Task>> getAll() {
        List<Task> task = taskService.findAll();
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> get(@PathVariable Long id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("")
    ResponseEntity<Task> post(@Valid @RequestBody Task task) {
        //todo task category id should not be provided might use DTO
        //task.setId(null);//todo maybe delete this
        taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    ResponseEntity<Task> put(@PathVariable Long id, @RequestBody Task task) {
        Task savedEntity = taskService.updateTask(id, task);
        return ResponseEntity.ok(savedEntity);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Task> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
