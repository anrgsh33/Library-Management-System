package com.example.lms.dto;

import com.example.lms.entity.BookEntity;
import com.example.lms.entity.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecordDto {
    private UserResDto user;
    private BookResDto book;
    private LocalDate borrowDate;
    private LocalDate returnDate;
}
