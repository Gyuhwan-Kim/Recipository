package com.example.recipository.domain;

import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.CommentResponseDto;
import com.example.recipository.dto.LinkDto;
import com.example.recipository.dto.RecipeDto;
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
public class Recipe extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;
    private String content;
    private String imagePath;
    private Long viewCount;
    private Long likeCount;
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> link;
    private String category;
    private boolean bePublic;
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("groupId asc, commentId asc")
    private List<Comment> commentList;
    @Column(nullable = false)
    private LocalDateTime modDate;

    @Override
    public LocalDateTime getRegDate() {
        return super.getRegDate();
    }

    public RecipeDto toDto(){
        return RecipeDto.builder()
                .contentId(contentId)
                .title(title)
                .writer(member.getName())
                .build();
    }

    public RecipeDto toDtoWithAll(){
        List<String> linkList = new ArrayList<String>();
        if(link != null){
            link.forEach(tmp -> {
                linkList.add(tmp.getLink());
            });
        }

        return RecipeDto.builder()
                .contentId(contentId)
                .title(title)
                .writer(member.getName())
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

    public void addViewCount(){
        this.viewCount = this.viewCount + 1;
    }

    public void updateRecipe(String newTitle, String newContent,
                             String newImagePath, boolean newBePublic,
                             List<Link> newLink){
        this.title = newTitle;
        this.content = newContent;
        if(newImagePath != null){
            this.imagePath = newImagePath;
        }
        this.bePublic = newBePublic;
        this.link = newLink;
    }
}
