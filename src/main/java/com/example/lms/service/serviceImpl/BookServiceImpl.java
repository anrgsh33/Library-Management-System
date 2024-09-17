package com.example.lms.service.serviceImpl;

import com.example.lms.dto.BookDto;
import com.example.lms.dto.BookResDto;
import com.example.lms.entity.BookEntity;
import com.example.lms.exception.CustomServiceException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.response.ResponseModel;
import com.example.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public ResponseModel<List<BookResDto>> getAllBooks() {
        try {
            List<BookEntity> bookList = bookRepository.findAll();
            List<BookResDto> bookResDtoList = bookList.stream()
                    .map(book -> new BookResDto(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getIsbn(),
                            book.getAvailableCopies()
                    ))
                    .collect(Collectors.toList());
            return new ResponseModel<>("Books retrieved successfully!", HttpStatus.OK, bookResDtoList);
        } catch (Exception e) {
            throw new CustomServiceException("Error while getting books");
        }
    }


    @Override
    public ResponseModel<BookResDto> getBookById(int id) {

        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        BookResDto bookResDto = new BookResDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getAvailableCopies()
        );
        return new ResponseModel<>("Book retrieved successfully!", HttpStatus.OK, bookResDto);
    }


    @Override
    public ResponseModel addBook(BookDto book) {
        try {
            BookEntity bookEntity=new BookEntity();
            bookEntity.setAuthor(book.getAuthor());
            bookEntity.setTitle(book.getTitle());
            bookEntity.setIsbn(book.getIsbn());
            bookEntity.setAvailableCopies(book.getAvailableCopies());

            bookRepository.save(bookEntity);

            return new ResponseModel("Book added successfully",HttpStatus.OK,null);

        }catch(Exception e){
            throw new CustomServiceException(e.getMessage());
        }
    }

    @Override
    public ResponseModel deleteBook(int id) {
        Optional<BookEntity> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.deleteById(id);

            return new ResponseModel("Book deleted successfully!",HttpStatus.OK,null);

        } else {
            throw new CustomServiceException("Some error occurred while deleting book");
        }
    }

    @Override
    public ResponseModel updateBook(int id, BookDto updatedBook) {
        Optional<BookEntity> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            BookEntity bookToUpdate = existingBook.get();
            bookToUpdate.setTitle(updatedBook.getTitle());
            bookToUpdate.setAuthor(updatedBook.getAuthor());
            bookToUpdate.setIsbn(updatedBook.getIsbn());
            bookRepository.save(bookToUpdate);

            return new ResponseModel<>("Book updated successfully!", HttpStatus.OK,null);

        } else {
            throw new ResourceNotFoundException("Book not found with id:"+id);
        }
    }



}
