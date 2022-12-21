package com.example.recipository.dto;

import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkDto {
    private Long id;
    private String link;
    private Long contentId;
}
