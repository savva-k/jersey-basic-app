package com.imsavva.jerseybasicsample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imsavva.jerseybasicsample.beans.Book;
import com.imsavva.jerseybasicsample.service.BookService;
import io.restassured.http.ContentType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static junit.framework.TestCase.assertTrue;

/**
 * Testing HelloWorldEndpoint with Rest Assured.
 *
 * @author Savva Kodeikin
 */
public class HelloWorldEnpointUsingRestAssured {

    private BookService bookService = BookService.getService();
    private static Server server;

    @BeforeClass
    public static void init() {
        // TODO initiate Jetty
    }

    @AfterClass
    public static void shutdown() {
        // TODO shutdown Jetty
    }

    @Before
    public void insertTestData() {
        bookService.addBook(new Book("Deborah Levy", "Hot Milk", 2016));
        bookService.addBook(new Book("Ottessa Moshfegh", "Eileen: A Novel", 2016));
        bookService.addBook(new Book("David Szalay", "All That Man Is", 2016));
    }

    @After
    public void removeAllBooks() {
        System.out.println("removing");
        bookService.removeAll();
    }

    @Test
    public void testAddingBook() {
        Map<String, String> book = new HashMap<>();
        book.put("author", "Arthur Conan Doyle");
        book.put("title", "The adventures of Sherlock Holmes");
        book.put("year", "1998");

        given().contentType(ContentType.JSON)
                .body(book)
                .when().post("/books").then()
                .statusCode(200);
    }

    @Test
    public void testGetAllBooks() {
        String response = get("/books").asString();
        Type listType = new TypeToken<ArrayList<Book>>(){}.getType();
        List<Book> books = new Gson().fromJson(response, listType);
        System.out.println(books.size());
        assertTrue("There must be 3 books in a response", books.size() == 3);
    }
}
