package com.imsavva.jerseybasicsample.service;

import com.imsavva.jerseybasicsample.beans.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple sample service that provides in-memory storage for Book POJOs.
 * As I don't use DI in this application, I had to make this service a singleton manually.
 *
 * @author Savva Kodeikin
 */
public class BookService {

    private static final BookService bookService;

    private Map<Integer, Book> books = new HashMap<>();
    private AtomicInteger bookId = new AtomicInteger(0);

    static {
        bookService = new BookService();
    }

    public BookService() {

    }

    /**
     * Get the service instance.
     *
     * @return the service instance
     */
    public static BookService getService() {
        return bookService;
    }

    /**
     * Get a book by its id.
     *
     * @param id id to find
     * @return Book POJO
     */
    public Book get(int id) {
        return books.get(id);
    }

    /**
     * Get all books.
     *
     * @return all Book POJOs
     */
    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    public void addBook(Book book) {
        int id = bookId.incrementAndGet();
        book.setId(id);
        books.put(id, book);
    }

    /**
     * Remove a book with specified id.
     *
     * @param bookId book id to remove
     * @return removed Book POJO
     */
    public Book remove(int bookId) {
        return books.remove(bookId);
    }

    /**
     * Save a given object into the repository.
     *
     * @param book
     */
    public void save(Book book) {
        books.put(book.getId(), book);
    }
}
