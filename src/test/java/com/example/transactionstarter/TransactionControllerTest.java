package com.example.transactionstarter;

import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanDatabase() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {

        String request = """
                {
                    "transactionId": "TXN001",
                    "customerId": "CUS001",
                    "amount": 1000.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "transactionStatus": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN001"))
                .andExpect(jsonPath("$.customerId").value("CUS001"))
                .andExpect(jsonPath("$.amount").value(1000.00));
    }

    @Test
    void shouldRejectInvalidTransaction() throws Exception {

        String request = """
                {
                    "transactionId": "",
                    "customerId": "CUS001",
                    "amount": -100,
                    "currency": "",
                    "transactionType": "PAYMENT",
                    "transactionStatus": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {

        String request = """
                {
                    "transactionId": "TXN002",
                    "customerId": "CUS001",
                    "amount": 500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "transactionStatus": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error")
                        .value("Transaction already exists: TXN002"));
    }

    @Test
    void shouldReturnNotFoundForUnknownTransaction() throws Exception {

        mockMvc.perform(get("/api/transactions/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Transaction not found: UNKNOWN"));
    }

    @Test
void shouldUpdateTransactionStatusSuccessfully() throws Exception {

    String createRequest = """
            {
                "transactionId": "TXN003",
                "customerId": "CUS002",
                "amount": 750.00,
                "currency": "INR",
                "transactionType": "PAYMENT",
                "transactionStatus": "PENDING"
            }
            """;

    mockMvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createRequest))
            .andExpect(status().isCreated());

    String updateRequest = """
            {
                "transactionStatus": "COMPLETED"
            }
            """;

    mockMvc.perform(patch("/api/transactions/TXN003/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionId").value("TXN003"))
            .andExpect(jsonPath("$.transactionStatus").value("COMPLETED"));
}


@Test
void shouldGetCustomerTransactionsSuccessfully() throws Exception {

    String request1 = """
            {
                "transactionId": "TXN004",
                "customerId": "CUS003",
                "amount": 100.00,
                "currency": "INR",
                "transactionType": "PAYMENT",
                "transactionStatus": "PENDING"
            }
            """;

    String request2 = """
            {
                "transactionId": "TXN005",
                "customerId": "CUS003",
                "amount": 200.00,
                "currency": "INR",
                "transactionType": "REFUND",
                "transactionStatus": "PENDING"
            }
            """;

    mockMvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request1))
            .andExpect(status().isCreated());

    mockMvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request2))
            .andExpect(status().isCreated());

    mockMvc.perform(get("/api/customers/CUS003/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].customerId").value("CUS003"))
            .andExpect(jsonPath("$[1].customerId").value("CUS003"));
}
}