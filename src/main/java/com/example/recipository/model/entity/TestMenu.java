package com.example.recipository.model.entity;

import com.example.recipository.domain.Link;
import lombok.*;
import org.aspectj.weaver.ast.Test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "test_menu")
@Entity
public class TestMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;
    private String writer;
    @OneToMany(mappedBy = "testMenu")
    private List<TestLink> link;

    public void addLink(TestLink testlink){
        if(link == null){
            link = new ArrayList<TestLink>();
        }

        link.add(testlink);
//        testlink.setTestMenu(this);
    }


}
