package com.expensetrack.expensetrack.transaction;

import com.expensetrack.expensetrack.security.SecurityConfig;
import com.expensetrack.expensetrack.transaction.TransactionController;
import com.expensetrack.expensetrack.transaction.TransactionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class) // This annotation is used to test the TransactionController class in a Spring
                                         // MVC context
@Import(SecurityConfig.class) // This annotation is used to import the SecurityConfig class for testing
@TestPropertySource(properties = {
        "jwt.secret-base64=Zm9vYmFyYmF6cXV4cXV4cXV4cXV4cXV4cXV4cXV4cXV4cXV4cXV4cQ==",
}) // This annotation is used to set the properties for the test, specifically the
   // JWT secret key
public class TransactionSecurityTest {
    @Autowired
    private MockMvc mockMvc; // This field is used to perform HTTP requests and assertions in the test
    @MockitoBean // This annotation is used to create a mock bean of the TransactionService class
                 // for testing
    private TransactionService transactionService;

    @Test
    void getTransactionsWithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTransactionsWithMalformedToken_ShouldReturn401()
            throws Exception {
        mockMvc.perform(get("/api/v1/transactions").header(HttpHeaders.AUTHORIZATION, "Bearer not-a-valid-token"))
                .andExpect(status().isUnauthorized());
    }
}
