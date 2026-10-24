package com.expensetrack.expensetrack.transaction;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.expensetrack.expensetrack.user.User;
import com.expensetrack.expensetrack.user.UserRepository;

@ExtendWith(MockitoExtension.class) // This annotation is used to enable Mockito support in JUnit 5 tests
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks // This annotation is used to inject the mocked dependencies into the
                 // TransactionService instance
    private TransactionService transactionService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User("sk@test.com", "test@1234", "Test User");
    } // This method is executed before each test case to set up the test data

    @Test
    void create_ShouldAssignAuthenticatedUserBeforeSavingTransaction() {

        // arrange
        CreateTransactionRequest request = new CreateTransactionRequest("Gloceries", new BigDecimal("500.00"),
                TransactionType.INCOME, LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArguments()[0]); // Return
                                                                                                                         // the
                                                                                                                         // transaction
                                                                                                                         // passed
                                                                                                                         // to
                                                                                                                         // the
                                                                                                                         // save
                                                                                                                         // method

        // act
        transactionService.create(request, userId);

        // assert
        // Verify that the transaction's user is set to the authenticated user before
        // saving
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        // ArgumentCaptor is used to capture the argument passed to the save method of
        // the transactionRepository mock
        // captor is used to capture the Transaction object that is passed to the save
        // method of the transactionRepository mock. This allows us to inspect the
        // captured Transaction object and verify its properties.
        verify(transactionRepository).save(captor.capture());
        Transaction savedTransaction = captor.getValue();

        assertSame(user, savedTransaction.getUser(), "The transaction's user should be the authenticated user");
        // assertSame is used to check if the two objects are the same instance. In this
        // case, it checks if the user associated with the saved transaction is the same
        // as the authenticated user.

    }

}
