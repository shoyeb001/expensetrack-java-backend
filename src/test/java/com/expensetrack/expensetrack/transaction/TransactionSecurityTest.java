package com.expensetrack.expensetrack.transaction;

import com.expensetrack.expensetrack.security.SecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(TransactionController.class) // This annotation is used to test the TransactionController class in a Spring
                                         // MVC context
@Import({ SecurityConfig.class, ServletWebSecurityAutoConfiguration.class }) // Import SecurityConfig and the
                                                                             // auto-configuration that provides
                                                                             // HttpSecurity bean
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
                mockMvc.perform(get("/api/v1/transactions").header(HttpHeaders.AUTHORIZATION,
                                "Bearer not-a-valid-token"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void getTransactionsWithAuthenticatedJwt_ShouldNotReturn() throws Exception {
                String userId = UUID.randomUUID().toString();
                mockMvc.perform(get("/api/v1/transactions").with(jwt().jwt(token -> token.subject(userId))))
                                .andExpect(status().isOk());
        }
}
