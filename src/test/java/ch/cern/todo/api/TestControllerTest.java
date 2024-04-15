package ch.cern.todo.api;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<Task> json;

    @MockBean
    private TaskService taskService;

    Task task;

    @BeforeEach
    public void setup() {
        task = getTask();
        task.setId(1L);
        given(taskService.save(any())).willReturn(task);
        given(taskService.getTask(any())).willReturn(task);
        given(taskService.findAll()).willReturn(Collections.singletonList(task));
    }

    @Test
    public void createTask() throws Exception {
        mvc.perform(post(new URI("/task"))
                        .content(json.write(task).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void listTasks() throws Exception {
        mvc.perform(get(new URI("/task"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void findTask() throws Exception {
        mvc.perform(get("/task/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTask() throws Exception {
        task.setDescription("buy milk");
        String json = objectMapper.writeValueAsString(task);
        mvc.perform(put(new URI("/task/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTask() throws Exception {
        mvc.perform(delete("/task/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Task getTask() {
        Task task = new Task();
        List<Task> tasks = List.of(task);
        TaskCategory tk = new TaskCategory(null, "groceries", "list of groceries", tasks);
        task.setName("groceries");
        task.setDeadline(Instant.ofEpochSecond(01012000));
        task.setDescription("list of groceries");
        task.setCategory(tk);
        return task;
    }

}
