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
    @Test
    void testCalculatePrice_TwoDistinctBooks_AppliesTwoBookDiscount() {
        double price = bookService.calculateMinimumPrice(new int[]{1, 1});
        assertEquals(95, price);
    }

    @Test
    void testCalculatePrice_TwoDistinct_TwoQuantities_Uses2BookDiscount() {
        double price = bookService.calculateMinimumPrice(new int[]{2, 2});
        assertEquals(190, price);

    }

    @Test
    void testCalculatePrice_ThreeDistinct_AppliesThreeBookDiscount() {
        double price = bookService.calculateMinimumPrice(new int[]{1, 1, 1});
        assertEquals(135, price);
    }

    @Test
    void testCalculatePrice_OptimalGrouping_8Books4Types_ComplexGrouping() {
        double price = bookService.calculateMinimumPrice(new int[]{2, 2, 2, 2});
        assertEquals(320, price);
    }

    @Test
    void testOptimalGrouping_5Books5Types_MaxDiscount() {
        double price = bookService.calculateMinimumPrice(new int[]{1,1,1,1,1});
        assertEquals(187.5, price);
    }

    @Test
    void testCalculatePrice_OptimalGrouping_2_2_2_1_1_ShouldPrefer4And4Over5And3() {
        double price = bookService.calculateMinimumPrice(new int[]{2,2,2,1,1});
        assertEquals(320, price);
    }

    @Test
    void testEmptyBasket_ReturnsZero() {
        double price = bookService.calculateMinimumPrice(new int[]{});
        assertEquals(0, price);
    }

}
