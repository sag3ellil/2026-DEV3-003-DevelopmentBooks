package com.store.books.service;

import com.store.books.dtos.BasketDTO;
import com.store.books.dtos.BookDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    BookService bookService;

    @Test
    public void testCalculatePrice_SingleBook_NoDiscount(){
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,1));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(50.0, price);
    }

    @Test
    public void testCalculatePrice_ThreeSameBooks_NoDiscount() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,3));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(150.0, price);
    }
    @Test
    void testCalculatePrice_TwoDistinctBooks_AppliesTwoBookDiscount() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,1));
        basketDTO.books().add(new BookDTO(2L,1));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(95, price);
    }

    @Test
    void testCalculatePrice_TwoDistinct_TwoQuantities_Uses2BookDiscount() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,2));
        basketDTO.books().add(new BookDTO(2L,2));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(190, price);

    }

    @Test
    void testCalculatePrice_ThreeDistinct_AppliesThreeBookDiscount() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,1));
        basketDTO.books().add(new BookDTO(2L,1));
        basketDTO.books().add(new BookDTO(3L,1));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(135, price);
    }

    @Test
    void testCalculatePrice_OptimalGrouping_8Books4Types_ComplexGrouping() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,2));
        basketDTO.books().add(new BookDTO(2L,2));
        basketDTO.books().add(new BookDTO(3L,2));
        basketDTO.books().add(new BookDTO(4L,2));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(320, price);
    }

    @Test
    void testOptimalGrouping_5Books5Types_MaxDiscount() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,1));
        basketDTO.books().add(new BookDTO(2L,1));
        basketDTO.books().add(new BookDTO(3L,1));
        basketDTO.books().add(new BookDTO(4L,1));
        basketDTO.books().add(new BookDTO(5L,1));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(187.5, price);
    }

    @Test
    void testCalculatePrice_OptimalGrouping_2_2_2_1_1_ShouldPrefer4And4Over5And3() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        basketDTO.books().add(new BookDTO(1L,2));
        basketDTO.books().add(new BookDTO(2L,2));
        basketDTO.books().add(new BookDTO(3L,2));
        basketDTO.books().add(new BookDTO(4L,1));
        basketDTO.books().add(new BookDTO(5L,1));
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(320, price);
    }

    @Test
    void testEmptyBasket_ReturnsZero() {
        BasketDTO basketDTO = new BasketDTO(new ArrayList<>());
        double price = bookService.calculateMinimumPrice(basketDTO.books());
        assertEquals(0, price);
    }

}
