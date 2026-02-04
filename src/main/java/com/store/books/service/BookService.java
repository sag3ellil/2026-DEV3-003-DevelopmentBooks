package com.store.books.service;

import com.store.books.dtos.BasketDTO;
import com.store.books.dtos.BookDTO;
import com.store.books.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    private static final double BOOK_PRICE = 50.0;
    private static final double[] DISCOUNTS = { 0, 1.0, 0.95, 0.9, 0.8, 0.75 };

    public double calculateMinimumPrice(List<BookDTO> basket) {
        if (isEmptyBasket(basket)) {
            return 0;
        }

        BasketState startState = BasketState.from(basket);

        Map<BasketState, Double> minimumCostByState = new HashMap<>();
        Queue<BasketState> statesToExplore = new ArrayDeque<>();

        minimumCostByState.put(startState, 0.0);
        statesToExplore.add(startState);

        double minimumTotalPrice = Double.MAX_VALUE;

        while (!statesToExplore.isEmpty()) {
            BasketState currentState = statesToExplore.poll();
            double currentCost = minimumCostByState.get(currentState);

            if (currentState.isEmpty()) {
                minimumTotalPrice = Math.min(minimumTotalPrice, currentCost);
                continue;
            }

            int distinctBooks = currentState.distinctCount();

            for (int setSize = 1; setSize <= distinctBooks; setSize++) {
                BasketState nextState = currentState.removeOneFromDistinctBooks(setSize);

                double nextCost = currentCost + calculateSetPrice(setSize);

                Double knownBestCost = minimumCostByState.get(nextState);
                if (knownBestCost == null || nextCost < knownBestCost) {
                    minimumCostByState.put(nextState, nextCost);
                    statesToExplore.add(nextState);
                }
            }
        }

        return minimumTotalPrice;
    }

    private double calculateSetPrice(int setSize) {
        return setSize * BOOK_PRICE * DISCOUNTS[setSize];
    }

    private int countDistinctBooks(List<BookDTO> basket) {
        int distinct = 0;
        for (BookDTO book : basket) {
            if (book.quantity() > 0) distinct++;
        }
        return distinct;
    }

    private List<BookDTO> removeOneFromDistinctBooks(List<BookDTO> basket, int setSize) {
        List<BookDTO> remainingBasket = new ArrayList<>(basket);

        int removed = 0;
        for (int i = 0; i < remainingBasket.size() && removed < setSize; i++) {
            BookDTO currentBook = remainingBasket.get(i);

            if (currentBook.quantity() > 0) {
                remainingBasket.set(i, new BookDTO(
                        currentBook.bookId(),
                        currentBook.quantity() - 1
                ));
                removed++;
            }
        }

        remainingBasket.removeIf(book -> book.quantity() == 0);
        return remainingBasket;
    }

    private boolean isEmptyBasket(List<BookDTO> basket) {
        return basket.isEmpty()
                || basket.stream().mapToInt(BookDTO::quantity).sum() == 0;
    }

    public Double placeOrder(BasketDTO basketDTO) {
        validateBasket(basketDTO);
        return calculateMinimumPrice(basketDTO.books());
    }

    private void validateBasket(BasketDTO basket) {
        List<BookDTO> books = basket.books();

        if (books.isEmpty() || books.stream().mapToDouble(BookDTO::quantity).sum() == 0) {
            throw new InvalidRequestException("Basket cannot be empty.");
        }
        if (books.size() != books.stream().map(BookDTO::bookId).distinct().count()) {
            throw new InvalidRequestException("Duplicate books in the basket.");
        }
        if (books.stream().map(BookDTO::quantity).anyMatch(quantity -> quantity < 0)) {
            throw new InvalidRequestException("Book quantity cannot be negative.");
        }
    }

    private static final class BasketState {
        private final int[] quantities;

        private BasketState(int[] quantities) {
            this.quantities = quantities;
        }

        static BasketState from(List<BookDTO> basket) {
            int[] normalized = basket.stream()
                    .mapToInt(BookDTO::quantity)
                    .filter(q -> q > 0)
                    .sorted()
                    .toArray();

            return new BasketState(normalized);
        }

        boolean isEmpty() {
            return quantities.length == 0;
        }

        int distinctCount() {
            return quantities.length;
        }

        BasketState removeOneFromDistinctBooks(int setSize) {
            int[] next = quantities.clone();

            for (int i = 0; i < setSize; i++) {
                next[next.length - 1 - i]--;
            }

            int[] normalized = Arrays.stream(next)
                    .filter(q -> q > 0)
                    .sorted()
                    .toArray();

            return new BasketState(normalized);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BasketState that)) return false;
            return Arrays.equals(quantities, that.quantities);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(quantities);
        }
    }
}
