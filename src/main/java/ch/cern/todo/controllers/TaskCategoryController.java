package ch.cern.todo.controllers;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.services.TaskCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskCategory")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskCategoryController {
    @Autowired
    TaskCategoryService taskCategoryService;

    @GetMapping("")
    ResponseEntity<List<TaskCategory>> getAll() {
        List<TaskCategory> taskCategories = taskCategoryService.findAll();
        return ResponseEntity.ok(taskCategories);
    }

    @GetMapping("/{id}")
    ResponseEntity<TaskCategory> get(@PathVariable Long id) {
        TaskCategory taskCategory = taskCategoryService.getTaskCategory(id);
        return ResponseEntity.ok(taskCategory);
    }

    @PostMapping("")
    ResponseEntity<TaskCategory> post(@Valid @RequestBody TaskCategory taskCategory) {
        taskCategory.setId(null);
        taskCategoryService.save(taskCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCategory);
    }

    @PutMapping("/{id}")
    ResponseEntity<TaskCategory> put(@PathVariable Long id, @Valid @RequestBody TaskCategory taskCategory) {
        TaskCategory savedEntity = taskCategoryService.updateTaskCategory(id, taskCategory);
        return ResponseEntity.ok(savedEntity);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<TaskCategory> delete(@PathVariable Long id) {
        taskCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
