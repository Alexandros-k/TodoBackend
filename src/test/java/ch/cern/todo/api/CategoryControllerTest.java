package ch.cern.todo.api;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.services.TaskCategoryService;
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
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<TaskCategory> json;

    @MockBean
    private TaskCategoryService taskCategoryService;

    TaskCategory taskCategory;

    @BeforeEach
    public void setup() {
        taskCategory = getTaskCategory();
        taskCategory.setId(1L);
        given(taskCategoryService.save(any())).willReturn(taskCategory);
        given(taskCategoryService.getTaskCategory(any())).willReturn(taskCategory);
        given(taskCategoryService.findAll()).willReturn(Collections.singletonList(taskCategory));
    }

    @Test
    public void createTaskCategory() throws Exception {
        mvc.perform(post(new URI("/taskCategory"))
                        .content(json.write(taskCategory).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    public void listCategoryTasks() throws Exception {
        mvc.perform(get("/taskCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.size()", is(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].tasks.size()", is(1)));
    }

    @Test
    public void findTaskCategory() throws Exception {
        mvc.perform(get("/taskCategory/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void updateTaskCategory() throws Exception {
        taskCategory.setName("superMarket");
        String json = objectMapper.writeValueAsString(taskCategory);
        mvc.perform(put(new URI("/taskCategory/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTaskCategory() throws Exception {
        mvc.perform(
                        delete("/taskCategory/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private TaskCategory getTaskCategory() {
        Task task = new Task();
        task.setName("groceries");
        task.setDeadline(Instant.ofEpochSecond(01012000));
        task.setDescription("list of groceries");
        List<Task> tasks = List.of(task);
        TaskCategory tk = new TaskCategory(null, "groceries", "list of groceries", tasks);
        task.setCategory(tk);
        return tk;
    }
}
