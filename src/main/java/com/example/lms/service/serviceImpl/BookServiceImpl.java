package com.example.lms.service.serviceImpl;

import com.example.lms.entity.BookEntity;
import com.example.lms.exception.CustomServiceException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookEntity> getAllBooks() {
        try {
            return bookRepository.findAll();
        }catch(Exception e){
            throw new CustomServiceException("Error while getting books");
        }
    }

    @Override
    public BookEntity getBookById(int id) {
        return  bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found with id:"+id));
    }


    @Override
    public String deleteBook(int id) {
        Optional<BookEntity> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.deleteById(id);
            return "Book deleted successfully!";
        } else {
            throw new CustomServiceException("Some error occurred while deleting book");
        }
    }

    @Override
    public String updateBook(int id, BookEntity updatedBook) {
        Optional<BookEntity> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            BookEntity bookToUpdate = existingBook.get();
            bookToUpdate.setTitle(updatedBook.getTitle());
            bookToUpdate.setAuthor(updatedBook.getAuthor());
            bookToUpdate.setIsbn(updatedBook.getIsbn());
            bookRepository.save(bookToUpdate);
            return "Book updated successfully!";
        } else {
            throw new ResourceNotFoundException("Book not found with id:"+id);
        }
    }

    @Override
    public String addBook(BookEntity book) {
        try {
            bookRepository.save(book);
            return "Book added successfully";
        }catch(Exception e){
            throw new CustomServiceException(e.getMessage());
        }
    }

}
