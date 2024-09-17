package com.example.lms.service;

import com.example.lms.entity.BookEntity;

import java.awt.print.Book;
import java.util.List;

public interface BookService {
    List<BookEntity> getAllBooks();
    BookEntity getBookById(int id);
    String addBook(BookEntity book);
    String deleteBook(int id);
    String updateBook(int id,BookEntity book);
}
