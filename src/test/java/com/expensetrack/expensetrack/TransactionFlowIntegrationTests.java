package com.expensetrack.expensetrack;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers   // Enables the Testcontainers framework to manage Docker container lifecycles automatically alongside the test suite
public class TransactionFlowIntegrationTests {
    private static final String JWT_SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="; //• Hardcodes a Base64-encoded mock secret key used by the application's token security layer to generate and validate signatures.
    @Container //• Tells Testcontainers to spin up a Docker container running an isolated PostgreSQL 16 database instance before tests execute.

    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine"); 


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("jwt.secret-base64", () -> JWT_SECRET);
    }

    @Autowired  //injects the pre-configured mock web client (mockMvc) and JSON mapper tool (objectMapper) into the test instance.
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userCanOnlyAccessTheirTransaction() throws Exception {
        String suffix = UUID.randomUUID().toString();
        String tokenA = registerAndLogin(
                "usera-" + suffix + "@example.com");

        String tokenB = registerAndLogin(
                "userb-" + suffix + "@example.com");
        String requestBody = """
                {
                     "title":"Integration test generation",
                     "amount": 500.00,
                     "transactionType":"EXPENSE",
                     "date":"%s"
                 }
                """.formatted(LocalDate.now());
        String createResponse = mockMvc
                .perform(post("/api/v1/transactions").header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        JsonNode created = objectMapper.readTree(createResponse);
        String transactionId = created.get("id").asText();

        // User A can retrieve their transaction.
        mockMvc.perform(
                get("/api/v1/transactions/{id}", transactionId)
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk());

        // User B cannot retrieve User A's transaction.
        mockMvc.perform(
                get("/api/v1/transactions/{id}", transactionId)
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isNotFound());

        // User B's list must not contain User A's transaction.
        mockMvc.perform(
                get("/api/v1/transactions")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));

    }

    private String registerAndLogin(String email) throws Exception {
        String password = "Stringpassword1234";
        String authBody = objectMapper.writeValueAsString(
                Map.of(
                        "email", email,
                        "password", password,
                        "name", "Test User"));

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(authBody))
                .andExpect(status().isCreated());

        String loginResponse = mockMvc
                .perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(authBody))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(loginResponse).get("accessToken").asText();

    }

}
