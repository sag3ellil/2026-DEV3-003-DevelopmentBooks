package com.store.books.service;

import com.store.books.dtos.BasketDTO;
import com.store.books.dtos.BookDTO;
import com.store.books.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private static final double BOOK_PRICE = 50.0;
    private static final double[] DISCOUNTS = { 0, 1.0, 0.95, 0.9, 0.8, 0.75 };

    public double calculateMinimumPrice(List<BookDTO> basket) {
        if (isEmptyBasket(basket)) {
            return 0;
        }

        double minimumTotalPrice = Double.MAX_VALUE;

        int distinctBooks = (int) basket.stream().filter(book -> book.quantity() > 0).count();

        for (int setSize = 1; setSize <= distinctBooks; setSize++) {
            List<BookDTO> remainingBasket = removeOneFromDistinctBooks(basket, setSize);

            double currentSetPrice = setSize * BOOK_PRICE * DISCOUNTS[setSize];
            double remainingPrice = calculateMinimumPrice(remainingBasket);

            minimumTotalPrice = Math.min(minimumTotalPrice, currentSetPrice + remainingPrice);
        }
        return minimumTotalPrice;
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
}
