package com.store.books.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookService {
    private static final double BOOK_PRICE = 50.0;
    private static final double[] DISCOUNTS = { 0, 1.0, 0.95, 0.9, 0.8, 0.75 };

    public double calculateMinimumPrice(int[] basket) {
        if (basket.length == 0 || Arrays.stream(basket).sum() == 0) {
            return 0;
        }
        double totalPrice = 0;

        List<Integer> bookCounts = new ArrayList<>();
        for (int basketItem : basket) {
            if (basketItem > 0) {
                bookCounts.add(basketItem);
            }
        }
        while (!bookCounts.isEmpty()) {
            int size = bookCounts.size();
            double total = size * BOOK_PRICE * DISCOUNTS[size];
            totalPrice += total;

            bookCounts.replaceAll(bookCount -> bookCount - 1);
            bookCounts.removeIf(bookCount -> bookCount == 0);
        }

        return totalPrice;
    }
}
