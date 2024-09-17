package com.example.lms.service;

import com.example.lms.dto.BookDto;
import com.example.lms.dto.BookResDto;
import com.example.lms.entity.BookEntity;
import com.example.lms.response.ResponseModel;
import org.springframework.http.ResponseEntity;

import java.awt.print.Book;
import java.util.List;

public interface BookService {
    ResponseModel<List<BookResDto>> getAllBooks();
    ResponseModel<BookResDto> getBookById(int id);
    ResponseModel addBook(BookDto book);
    ResponseModel deleteBook(int id);
    ResponseModel updateBook(int id,BookDto book);
}
