package com.example.lms.service;

import com.example.lms.dto.BorrowRecordDto;
import com.example.lms.entity.BorrowRecordEntity;
import com.example.lms.response.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface BorrowRecordService {
    ResponseModel<List<BorrowRecordDto>> getBorrowedBooks();
    ResponseModel addBorrowRecord(int id);

}
