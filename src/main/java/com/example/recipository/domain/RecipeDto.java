package com.example.recipository.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
                .build();
    }
}
