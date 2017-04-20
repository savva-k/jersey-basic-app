package com.imsavva.jerseybasicsample.endpoint;

import com.imsavva.jerseybasicsample.beans.Book;
import com.imsavva.jerseybasicsample.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A sample JAX-RS CRUD controller.
 *
 * @author Savva Kodeikin
 */
@Path("/")
public class HelloWorldEndpoint {

    private BookService bookService = BookService.getService();

    /**
     * Get all the entities.
     *
     * @return JSON response containing Book objects
     */
    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(bookService.getAll()).build();
    }

    /**
     * Get a book by its id.
     *
     * @param bookId book id to search
     * @return Book object
     */
    @GET
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int bookId) {
        return getBookOrNotFoundResponse(bookService.get(bookId));
    }

    /**
     * Store a new book.
     *
     * @param book JSON string that can be converted into Book object
     * @return just OK status
     */
    @POST
    @Path("/books")
    public Response addBook(Book book) {
        bookService.addBook(book);
        return Response.ok().build();
    }

    /**
     * Update an existing book.
     *
     * @param bookId book id
     * @param newBook JSON string containing a book
     * @return OK status if the book was found, otherwise NOT_FOUND
     */
    @PUT
    @Path("/books/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int bookId, Book newBook) {
        Book book = bookService.get(bookId);

        if (book != null) {
            book.setAuthor(newBook.getAuthor());
            book.setTitle(newBook.getTitle());
            book.setYear(newBook.getYear());
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        bookService.save(book);
        return Response.ok().build();
    }

    /**
     * Remove a book with specified id.
     *
     * @param bookId a book id to remove
     * @return removed Book object
     */
    @DELETE
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") int bookId) {
        return getBookOrNotFoundResponse(bookService.remove(bookId));
    }

    private Response getBookOrNotFoundResponse(Book book) {
        if (book != null) {
            return Response.ok().entity(book).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
