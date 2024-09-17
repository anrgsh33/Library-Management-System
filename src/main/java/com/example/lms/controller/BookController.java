package com.example.lms.controller;

import com.example.lms.dto.BookDto;
import com.example.lms.dto.BookResDto;
import com.example.lms.dto.UserResDto;
import com.example.lms.entity.BookEntity;
import com.example.lms.response.ResponseModel;
import com.example.lms.service.BookService;
import com.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    ResponseEntity<ResponseModel<List<BookResDto>>> getAllBooks(){
        ResponseModel<List<BookResDto>> response= bookService.getAllBooks();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path="/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseModel<List<UserResDto>>> getAllUsers() {
        ResponseModel<List<UserResDto>> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/books/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseModel<BookResDto>> getBookById(@PathVariable int id) {
        ResponseModel<BookResDto> response = bookService.getBookById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping(path = "/books")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseModel> addBook(@RequestBody BookDto book) {
        ResponseModel response= bookService.addBook(book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping(path = "/books/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseModel> updateBook(@PathVariable("id") int id, @RequestBody BookDto book) {
        ResponseModel response = bookService.updateBook(id, book);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping(path = "/books/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseModel> deleteBook(@PathVariable("id") int id) {
        ResponseModel response = bookService.deleteBook(id);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
