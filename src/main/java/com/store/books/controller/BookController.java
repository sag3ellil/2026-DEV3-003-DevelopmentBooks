package com.store.books.controller;

import com.store.books.dtos.BasketDTO;
import com.store.books.dtos.PriceResponseDTO;
import com.store.books.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    private BookService bookService;
    @PostMapping("/discounts")
    public ResponseEntity<PriceResponseDTO> calculateDiscount(@RequestBody BasketDTO basketDTO) {
        double price = bookService.placeOrder(basketDTO);
        return ResponseEntity.ok(new PriceResponseDTO(price));
    }
}
