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

    public double calculateMinimumPrice(List<BookDTO> books) {
        if (books.size() == 0 || books.stream().mapToInt(BookDTO::quantity).sum() == 0) {
            return 0;
        }
        double minPrice = Double.MAX_VALUE;

        int distinctBooks = (int) books.stream().filter(book -> book.quantity() > 0).count();

        for (int setSize = 1; setSize <= distinctBooks; setSize++) {
            List<BookDTO> remainingBooks = new ArrayList<>(books);
            int removed = 0;
            for (int i = 0; i < remainingBooks.size() && removed < setSize; i++) {
                BookDTO current = remainingBooks.get(i);
                if (current.quantity() > 0) {
                    BookDTO updated = new BookDTO(
                            current.bookId(),
                            current.quantity() - 1
                    );
                    remainingBooks.set(i, updated);
                    removed++;
                }
            }

            remainingBooks.removeIf(book -> book.quantity() == 0);

            double setPrice = setSize * BOOK_PRICE * DISCOUNTS[setSize];
            double remainingPrice = calculateMinimumPrice(remainingBooks);

            minPrice = Math.min(minPrice, setPrice + remainingPrice);
        }
        return minPrice;
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
