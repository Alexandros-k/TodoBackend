package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Test
    @DisplayName("throws exception in case task not found")
    void getTaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.getTask(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("task is found")
    void getTaskFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(new Task()));
        Assertions.assertDoesNotThrow(() -> taskService.getTask(1L));
        verify(taskRepository, times(1)).findById(1L);
        Task task = taskService.getTask(1L);
        Assertions.assertNotNull(task);
    }


    @Test
    void findAll() {
        taskService.findAll();
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}