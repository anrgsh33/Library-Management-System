package com.example.lms.service.serviceImpl;

import com.example.lms.dto.BookResDto;
import com.example.lms.dto.BorrowRecordDto;
import com.example.lms.dto.UserResDto;
import com.example.lms.entity.BookEntity;
import com.example.lms.entity.BorrowRecordEntity;
import com.example.lms.entity.UserEntity;
import com.example.lms.exception.CustomServiceException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.BookRepository;
import com.example.lms.repository.BorrowRecordRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.response.ResponseModel;
import com.example.lms.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService{
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseModel<List<BorrowRecordDto>> getBorrowedBooks() {
        try {
            List<BorrowRecordEntity> borrowRecords = borrowRecordRepository.findAll();
            List<BorrowRecordDto> borrowRecordDtoList = borrowRecords.stream()
                    .map(record -> new BorrowRecordDto(
                            new UserResDto(
                                    record.getUserEntity().getId(),
                                    record.getUserEntity().getName(),
                                    record.getUserEntity().getEmail()
                            ),
                            new BookResDto(
                                    record.getBookEntity().getId(),
                                    record.getBookEntity().getTitle(),
                                    record.getBookEntity().getAuthor(),
                                    record.getBookEntity().getIsbn(),
                                    record.getBookEntity().getAvailableCopies()
                            ),
                            record.getBorrowDate(),
                            record.getReturnDate()
                    ))
                    .collect(Collectors.toList());
            return new ResponseModel<>("Borrow Records retrieved successfully", HttpStatus.OK, borrowRecordDtoList);
        } catch (Exception e) {
            throw new CustomServiceException("Some error occurred while fetching the borrowed books list");
        }
    }


    @Override
    public ResponseModel addBorrowRecord(int id){

        BookEntity book=bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Book not found with id:"+id));

        try{
            if(book.getAvailableCopies()<=0){
                throw new CustomServiceException("No copy of book is available");
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<UserEntity> userOptional = userRepository.findByEmail(email);

            if(userOptional.isPresent()){
                BorrowRecordEntity record = new BorrowRecordEntity();
                record.setUserEntity(userOptional.get());
                record.setBookEntity(book);
                record.setBorrowDate(LocalDate.now());
                record.setReturnDate(LocalDate.now().plusDays(10));

                borrowRecordRepository.save(record);
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                bookRepository.save(book);
            }else{
                throw new CustomServiceException("Username not found");
            }

            return new ResponseModel("Borrow record added successfully", HttpStatus.OK  ,null);
        }catch(Exception e){
            throw new CustomServiceException(e.getMessage());
        }
    }


}
