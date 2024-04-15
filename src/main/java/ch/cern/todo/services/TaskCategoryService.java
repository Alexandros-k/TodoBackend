package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.repositories.TaskCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskCategoryService {

    @Autowired
    private final TaskCategoryRepository taskCategoryRepository;

    public TaskCategory getTaskCategory(Long id) {
        Optional<TaskCategory> taskCategoryOptional = taskCategoryRepository.findById(id);
        return taskCategoryOptional.orElseThrow(EntityNotFoundException::new);
    }

    public List<TaskCategory> findAll() {
        return taskCategoryRepository.findAll();
    }

    public TaskCategory save(TaskCategory taskCategory) {
        return taskCategoryRepository.save(taskCategory);
    }

    public TaskCategory updateTaskCategory(Long id, TaskCategory taskCategory) {
        return taskCategoryRepository.findById(id)
                .map(taskToBeUpdated -> {
                    taskToBeUpdated.setDescription(taskCategory.getDescription());
                    taskToBeUpdated.setName(taskCategory.getName());
                    return taskCategoryRepository.save(taskToBeUpdated);
                }).orElseThrow(EntityNotFoundException::new);
    }


    public void delete(Long id) {
        TaskCategory taskCategory = taskCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        taskCategoryRepository.delete(taskCategory);
    }
}
