package com.example.lms.controller;

import com.example.lms.dto.BorrowRecordDto;
import com.example.lms.entity.BorrowRecordEntity;
import com.example.lms.response.ResponseModel;
import com.example.lms.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BorrowRecordController {
    @Autowired
    private BorrowRecordService borrowRecordService;

    @GetMapping(path="/borrowed-books")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseModel<List<BorrowRecordDto>>> getBorrowedBooks(){
        ResponseModel<List<BorrowRecordDto>>response=borrowRecordService.getBorrowedBooks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path="/borrow-book/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseModel>addBorrowRecord(@PathVariable int id){
        ResponseModel response= borrowRecordService.addBorrowRecord(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
