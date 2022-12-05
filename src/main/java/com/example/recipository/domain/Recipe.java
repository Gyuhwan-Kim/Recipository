package com.example.recipository.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "recipe")
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;
    private String title;
    private String writer;
    private String content;
    private String imagePath;
    private Long viewCount;
    private Long likeCount;
    @Transient
    private List<Link> link;
    private String category;
    private boolean bePublic;

    public RecipeDto toDto(){
        List<String> linkList = new ArrayList<String>();
        if(link != null){
            link.forEach(tmp -> {
                linkList.add(tmp.getLink());
            });
        }

        return RecipeDto.builder()
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

    public void setRecipeAtLink(){
        link.forEach(tmp -> {
            tmp.setRecipe(this);
        });
    }
}
