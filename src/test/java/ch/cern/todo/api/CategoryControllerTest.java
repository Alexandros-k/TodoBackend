package ch.cern.todo.api;

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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CategoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<TaskCategory> json;

    @MockBean
    private TaskCategoryService taskCategoryService;


    TaskCategory taskCategory;

    /**
     * Creates pre-requisites for testing, such as an example TaskCategory.
     */
    @BeforeEach
    public void setup() {
        taskCategory = getTaskCategory();
        taskCategory.setId(1L);
        given(taskCategoryService.save(any())).willReturn(taskCategory);
        given(taskCategoryService.getTaskCategory(any())).willReturn(taskCategory);
        given(taskCategoryService.findAll()).willReturn(Collections.singletonList(taskCategory));
    }

    /**
     * Tests for successful creation of new TaskCategory in the system
     *
     * @throws Exception when TaskCategory creation fails in the system
     */
    @Test
    public void createTaskCategory() throws Exception {
        mvc.perform(post(new URI("/taskCategory"))
                        .content(json.write(taskCategory).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of TaskCategories.
     *
     * @throws Exception if the read operation of the TaskCategories list fails
     */
    @Test
    public void listCars() throws Exception {

        mvc.perform(get("/taskCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // .andExpect(jsonPath("$._embedded.taskCategoryList.size()", is(1)));
    }

    /**
     * Tests the read operation for a single TaskCategory by ID.
     *
     * @throws Exception if the read operation for a single TaskCategory fails
     */
    @Test
    public void findTaskCategory() throws Exception {

        mvc.perform(get("/taskCategory/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void updateTaskCategory() throws Exception {
        taskCategory.setName("superMarket");
        String json = new ObjectMapper().writeValueAsString(taskCategory);
        mvc.perform(put(new URI("/taskCategory/1"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk());

    }

    /**
     * Tests the deletion of a single TaskCategory by ID.
     *
     * @throws Exception if the delete operation of a TaskCategory fails
     */
    @Test
    public void deleteTaskCategory() throws Exception {

        mvc.perform(
                        delete("/taskCategory/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Creates an example TaskCategory object for use in testing.
     *
     * @return an example TaskCategory object
     */
    private TaskCategory getTaskCategory() {
        TaskCategory tk = new TaskCategory(1L, "groceries", "list of groceries");
        return tk;
    }
}
