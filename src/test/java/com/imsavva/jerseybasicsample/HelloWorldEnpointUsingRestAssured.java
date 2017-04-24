package com.imsavva.jerseybasicsample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imsavva.jerseybasicsample.beans.Book;
import com.imsavva.jerseybasicsample.service.BookService;
import io.restassured.http.ContentType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
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
    public static void init() throws Exception {
        server = new Server(8080);
        server.setStopAtShutdown(true);
        WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/");
        webAppContext.setClassLoader(HelloWorldEnpointUsingRestAssured.class.getClassLoader());
        server.setHandler(webAppContext);

        server.start();
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.stop();
    }

    @Before
    public void insertTestData() {
        bookService.addBook(new Book("Deborah Levy", "Hot Milk", 2016));
        bookService.addBook(new Book("Ottessa Moshfegh", "Eileen: A Novel", 2016));
        bookService.addBook(new Book("David Szalay", "All That Man Is", 2016));
    }

    @After
    public void removeAllBooks() {
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

        assertTrue("There must be 3 books in a response", books.size() == 3);
    }

    @Test
    public void testGetBookById() {
        String id = String.valueOf(bookService.getAll().get(0).getId());
        String response = get("/books/" + id).asString();
        Book book = new Gson().fromJson(response, Book.class);

        assertNotNull("Book shouldn't be null", book);
    }

    @Test
    public void testUpdateBook() {
        String url = "/books/" + String.valueOf(bookService.getAll().get(0).getId());
        String response = get(url).asString();

        int newYear = 2017;
        String newAuthor = "Savva Kodeikin";
        String newTitle = "Everebody knows how to work with JAX-RS but anyway";

        Book book = new Gson().fromJson(response, Book.class);
        book.setYear(newYear);
        book.setAuthor(newAuthor);
        book.setTitle(newTitle);

        given().contentType(ContentType.JSON)
                .body(book)
                .when().put(url).then()
                .statusCode(200);

        response = get(url).asString();
        book = new Gson().fromJson(response, Book.class);

        assertEquals("Authors should be equal", newAuthor, book.getAuthor());
        assertEquals("Titles should be equal", newTitle, book.getTitle());
        assertTrue("Year fields should be equal", newYear == book.getYear());
    }

    @Test
    public void testDeleteBook() {
        String url = "/books/" + String.valueOf(bookService.getAll().get(0).getId());
        delete(url);

        given().when().get(url).then().statusCode(404);
    }
}
