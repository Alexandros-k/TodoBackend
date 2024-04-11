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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Task> json;

    @MockBean
    private TaskService taskService;


    Task task;

    /**
     * Creates pre-requisites for testing, such as an example task.
     */
    @BeforeEach
    public void setup() {
        task = getTask();
        task.setId(1L);
        given(taskService.save(any())).willReturn(task);
        given(taskService.getTask(any())).willReturn(task);
        given(taskService.findAll()).willReturn(Collections.singletonList(task));
    }

    /**
     * Tests for successful creation of new task in the system
     *
     * @throws Exception when task creation fails in the system
     */
    @Test
    public void createTask() throws Exception {
        mvc.perform(post(new URI("/task"))
                        .content(json.write(task).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     *
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listTasks() throws Exception {

        mvc.perform(get(new URI("/task"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //.andExpect(jsonPath("$._embedded.task.size()", is(1)))
    }

    /**
     * Tests the read operation for a single task by ID.
     *
     * @throws Exception if the read operation for a single task fails
     */
    @Test
    public void findTask() throws Exception {
        /**
         * TODO: Add a test to check that the `get` method works by calling
         *   a vehicle by ID. This should utilize the task from `getTask()` below.
         */
        mvc.perform(get("/task/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void updateTask() throws Exception {
        task.setDescription("buy milk");
        String json = new ObjectMapper().writeValueAsString(task);
        mvc.perform(put(new URI("/task/1"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk());

    }

    /**
     * Tests the deletion of a single task by ID.
     *
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteTask() throws Exception {
        mvc.perform(delete("/task/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Creates an example Task object for use in testing.
     *
     * @return an example Task object
     */
    private Task getTask() {
        TaskCategory tk = new TaskCategory(1L, "groceries", "list of groceries");
        Task task = new Task();
        task.setId(1L);
        task.setName("groceries");
        task.setDeadline((LocalDateTime.parse("2022-04-12T15:30:00")));
        task.setDescription("list of groceries");
        task.setCategory(tk);
        return task;
    }


}
