package com.example.recipository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto {
    private int startPageNum;
    private int pageNum;
    private int endPageNum;
    private int totalPageNum;
}
