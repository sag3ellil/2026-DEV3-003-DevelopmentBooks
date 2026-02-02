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
        double minPrice = Double.MAX_VALUE;

        int distinctBooks = (int) Arrays.stream(basket).filter(q -> q > 0).count();

        for (int setSize = 1; setSize <= distinctBooks; setSize++) {
            int[] remainingBasket = Arrays.copyOf(basket, basket.length);
            int removed = 0;
            for (int i = 0; i < remainingBasket.length && removed < setSize; i++) {
                if (remainingBasket[i] > 0) {
                    remainingBasket[i]--;
                    removed++;
                }
            }

            double setPrice = setSize * BOOK_PRICE * DISCOUNTS[setSize];

            double remainingPrice = calculateMinimumPrice(remainingBasket);

            minPrice = Math.min(minPrice, setPrice + remainingPrice);
        }
        return minPrice;
    }
}
