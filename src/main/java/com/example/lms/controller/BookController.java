package com.example.lms.controller;

import com.example.lms.entity.BookEntity;
import com.example.lms.entity.UserEntity;
import com.example.lms.service.BookService;
import com.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;
    @GetMapping(path="/books")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    List<BookEntity> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping(path="/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/books/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BookEntity> getBookById(@PathVariable int id) {
        BookEntity book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);

    }

    @PostMapping(path = "/books")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody BookEntity book) {
        String response= bookService.addBook(book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping(path = "/books/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> updateBook(@PathVariable("id") int id, @RequestBody BookEntity book) {
        String result = bookService.updateBook(id, book);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @DeleteMapping(path = "/books/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable("id") int id) {
        String result = bookService.deleteBook(id);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
