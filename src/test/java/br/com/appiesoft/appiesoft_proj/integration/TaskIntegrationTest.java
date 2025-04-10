package br.com.appiesoft.appiesoft_proj.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornar401AoListarTarefasSemToken() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar400AoCriarTarefaSemCamposObrigatorios() throws Exception {
        String payload = """
                {
                  "title": "",
                  "priority": null,
                  "dueDate": null
                }
                """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    void deveCriarETrazerTarefaAutenticado() throws Exception {
        String userPayload = """
                {
                    "name": "Test User",
                    "email": "test@email.com,
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userPayload))
                .andExpect(status().isOk());

        String loginPayload = """

                {
                    "email": "test@email.com",
                    "password": "123456"
                }
                """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = loginResult.getResponse().getContentAsString();
        String token = responseJson.split("\"token\":\"")[1].split("\"")[0];

        String taskPayload = """
                {
                  "title": "Tarefa Teste",
                  "description": "Descrição de teste",
                  "dueDate": "2025-04-15",
                  "priority": "MEDIUM"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tarefa Teste"));

        mockMvc.perform(get("/api/tasks")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
