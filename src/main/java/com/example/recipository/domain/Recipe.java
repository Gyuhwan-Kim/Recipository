package com.example.recipository.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "recipe")
@Entity
public class Recipe extends BaseTime{
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
    @Column(nullable = false)
    private LocalDateTime modDate;

    @Override
    public LocalDateTime getRegDate() {
        return super.getRegDate();
    }

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
                .regDate(getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modDate(getModDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public void setRecipeAtLink(){
        link.forEach(tmp -> {
            tmp.setRecipe(this);
        });
    }
}
