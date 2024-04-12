package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;

    public Task getTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        return taskOptional.orElseThrow(EntityNotFoundException::new);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        if (task.getId() != null) {
            return taskRepository.findById(task.getId())
                    .map(taskToBeUpdated -> {
                        taskToBeUpdated.setDescription(task.getDescription());
                        taskToBeUpdated.setName(task.getName());
                        return taskRepository.save(taskToBeUpdated);
                    }).orElseThrow(EntityNotFoundException::new);
        }
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        taskRepository.delete(task);
    }
}
