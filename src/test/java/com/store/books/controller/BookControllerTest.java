package com.store.books.controller;


import com.store.books.dtos.BasketDTO;
import com.store.books.exception.InvalidRequestException;
import com.store.books.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;


    @Test
    void shouldReturn200_whenBasketIsValid() throws Exception {
        given(bookService.placeOrder(any()))
                .willReturn(95.00);

        String jsonRequest = """
                {
                  "books": [
                    { "bookId": 1, "quantity": 1 },
                    { "bookId": 2, "quantity": 1 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/book/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.price").value(95.00));
    }

    @Test
    void shouldReturn400_whenQuantityIsInvalid() throws Exception {
        given(bookService.placeOrder(any(BasketDTO.class)))
                .willThrow(new InvalidRequestException("Basket cannot be empty."));
        String jsonRequest = """
                {
                  "books": [
                    { "bookId": "1", "quantity": 0 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/book/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/book/discounts"));
    }



    @Test
    void shouldReturn500_whenServiceThrowsUnexpectedException() throws Exception {
        given(bookService.placeOrder(any()))
                .willThrow(new RuntimeException("Something bad happened"));

        String jsonRequest = """
                {
                  "books": [
                    { "bookId": 1, "quantity": 1 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/book/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/api/book/discounts"));
    }

    @Test
    void shouldReturn400_whenBasketIsInvalid() throws Exception {
        given(bookService.placeOrder(any(BasketDTO.class)))
                .willThrow(new InvalidRequestException("Basket cannot be empty."));

        String jsonRequest = """
            {
              "books": []
            }
            """;

        mockMvc.perform(post("/api/book/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
}
