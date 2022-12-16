package com.example.recipository.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {
    private Long contentId;
    private String title;
    private String writer;
    private String content;
    private String imagePath;
    private Long viewCount;
    private Long likeCount;
    private List<String> link;
    private String category;
    private boolean bePublic;
    private String regDate;
    private String modDate;

    public Recipe toEntity(){
        List<Link> linkList = new ArrayList<>();
        if(link != null) {
            link.forEach(tmp -> {
                Link link = Link.builder()
                        .link(tmp)
                        .build();
                linkList.add(link);
            });
        }

        LocalDateTime ldtModDate;
        if(this.modDate != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ldtModDate = LocalDateTime.parse(this.modDate, formatter);
        } else {
            ldtModDate = LocalDateTime.now();
        }

        return Recipe.builder()
                .contentId(contentId)
                .title(title)
                .writer(writer)
                .content(content)
                .imagePath(imagePath)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .link(linkList)
                .category(category)
                .bePublic(bePublic)
                .modDate(ldtModDate)
                .build();
    }
}
