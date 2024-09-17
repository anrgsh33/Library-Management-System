package com.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResDto {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int availableCopies;
}
