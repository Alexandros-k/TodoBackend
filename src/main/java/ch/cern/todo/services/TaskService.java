package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.repositories.TaskCategoryRepository;
import ch.cern.todo.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;

    private final TaskCategoryRepository taskCategoryRepository;

    public Task getTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        return taskOptional.orElseThrow(EntityNotFoundException::new);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        TaskCategory taskCategory = taskCategoryRepository.findById(task.getCategory().getId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        task.setCategory(taskCategory);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        return taskRepository.findById(id)
                .map(taskToBeUpdated -> {
                    taskToBeUpdated.setDescription(task.getDescription());
                    taskToBeUpdated.setName(task.getName());
                    return taskRepository.save(taskToBeUpdated);
                }).orElseThrow(EntityNotFoundException::new);
    }

    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        taskRepository.deleteAllByIdInBatch(List.of(id));
    }
}
