package com.store.books.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    BookService bookService;

    @Test
    public void testCalculatePrice_SingleBook_NoDiscount(){
        double price = bookService.calculateMinimumPrice(new int[] {1});
        assertEquals(50.0, price);
    }

    @Test
    public void testCalculatePrice_ThreeSameBooks_NoDiscount() {
        double price = bookService.calculateMinimumPrice(new int[] {3});
        assertEquals(150.0, price);
    }

}
