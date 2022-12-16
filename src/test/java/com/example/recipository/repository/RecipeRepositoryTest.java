package com.example.recipository.repository;

import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import com.example.recipository.dto.RecipeDto;
import com.example.recipository.model.entity.TestLink;
import com.example.recipository.model.entity.TestMenu;
import com.example.recipository.service.RecipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private RecipeServiceImpl menuService;
    
    @PersistenceUnit
    EntityManagerFactory emf;

    @PersistenceContext
    EntityManager em;

    @Test
//    @Transactional
    public void test(){
        List<String> link = new ArrayList<String>();
        link.add("zzz");
        link.add("xxx");

        // 이건 성공
//        MenuDto menuDto = new MenuDto();
//        menuDto.setWriter("test4");
//
//        Menu menu = menuDto.toEntity();
//
//        List<Link> linkList = new ArrayList<Link>();
//
//        link.forEach(tmp -> {
//            Link newLink = Link.builder()
//                    .link(tmp)
//                    .menu(menu)
//                    .build();
//            linkList.add(newLink);
//            menu.setLink(linkList);
//        });
//
//        menuRepository.save(menu);

        // 실패 파트 (에러남 -> 실패 아니었음. toStirng 이랑 hashcode 오류)
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setWriter("test11");
        recipeDto.setLink(link);

        Recipe recipe = recipeDto.toEntity();

        recipe.getLink().forEach(tmp -> {
//            System.out.println(tmp);
//            tmp.setMenu(menu);
        });
//
//        System.out.println(menu);

        recipeRepository.save(recipe);

        // Entity Manager 사용 성공
        // 각 액세스마다 생성한다.
//        EntityManager em = emf.createEntityManager();
//        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행되어야 한다.
//        EntityTransaction tx = em.getTransaction();
//        tx.begin();
//
//        MenuDto menuDto = new MenuDto();
//        menuDto.setWriter("test4");
//        menuDto.setLink(link);
//
//        Menu menu = menuDto.toEntity();
//
//        menu.getLink().forEach(tmp -> {
//            tmp.setMenu(menu);
//        });
//
//        em.persist(menu);
//
//        tx.commit();
//        em.close();
    }


    // 삭제
    @Test
    public void test2(){
        recipeRepository.deleteById(4L);
    }

    // 추가(수정)
    @Test
    @Transactional//(readOnly = true)
    public void test3(){
        List<String> linkList = new ArrayList<String>();
        linkList.add("zxc");
        linkList.add("vv");
//        linkList.add("ff");

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setContentId(3L);
        recipeDto.setWriter("test3");
        recipeDto.setLink(linkList);

        Recipe recipe = recipeDto.toEntity();

        Recipe dbRecipe = recipeRepository.getRecipeByContentId(recipe.getContentId());

        System.out.println(dbRecipe.toDto());
        dbRecipe.getLink().forEach(tmp -> {
           Long id = tmp.getId();
           int index = dbRecipe.getLink().indexOf(tmp);
//           menu.getLink().get(index).setId(id);
//           menu.getLink().get(index).setMenu(menu);
        });
//
//        menuRepository.save(menu);
//        linkRepository.saveAll(menu.getLink());

        // Link 만 바꾸기 실패
//        Menu tmpMenu = new Menu();
//        tmpMenu.setContentId(3L);
//
//        List<Link> dbLink = linkRepository.findAllByMenu(tmpMenu);
//        dbLink.forEach(tmp ->{
//            System.out.println(tmp.toDto());
//            tmp.setLink(linkList.get(dbLink.indexOf(tmp)));
//            System.out.println(tmp.toDto());
//        });
//
//        linkRepository.saveAll(dbLink);
    }

    @Test
    @Transactional//(readOnly = true)
    public void test4(){
        List<String> link = new ArrayList<String>();
        link.add("poi");
        link.add("lk");

        Recipe recipe = recipeRepository.getRecipeByContentId(3L);
        System.out.println("------------------");
        System.out.println(recipe.toDto());

    }

    @Test
    public void test5(){
        Recipe recipe1 = new Recipe();

//        menu1.setTitle("test1");

        System.out.println(recipe1);
        System.out.println(recipe1.hashCode());

//        menu1.setTitle("test2");

        System.out.println(recipe1);
        System.out.println(recipe1.hashCode());

        System.out.println("------------------");
    }

    @Autowired
    TestMenuRepository testMenuRepository;
    @Autowired
    TestLinkRepository testLinkRepository;

    // OneToMany 단방향
    @Test
    public void test6() {
        List<String> linkList = new ArrayList<String>();
        linkList.add("abc");
        linkList.add("de");

        TestMenu testMenu = new TestMenu();
        testMenu.setWriter("test1");

        linkList.forEach(tmp -> {
            TestLink testLink = new TestLink();
            testLink.setLink(tmp);
            testMenu.addLink(testLink);

//            testLinkRepository.save(testLink);
        });

        testMenuRepository.save(testMenu);
    }

    // ManyToOne 단방향
    @Test
    @Transactional
    public void test7() {
        List<String> linkList = new ArrayList<String>();
        linkList.add("abc");
        linkList.add("de");

        TestMenu testMenu = new TestMenu();
        testMenu.setWriter("test18");

        testMenuRepository.save(testMenu);

        linkList.forEach(tmp -> {
            TestLink testLink = new TestLink();
            testLink.setLink(tmp);
            testLink.setTestMenu(testMenu);

            testLinkRepository.save(testLink);
        });
    }

    // 다대일 양방향
    @Test
    @Transactional
    @Rollback(false)
    public void test8() {
        List<String> linkList = new ArrayList<String>();
        linkList.add("aaa");
        linkList.add("bb");

        TestMenu testMenu = new TestMenu();
        testMenu.setWriter("test5");

//        testMenuRepository.save(testMenu);

        TestMenu tmpMenu = new TestMenu();
        tmpMenu.setContentId(6L);

        List<TestLink> tmpList = testLinkRepository.findAllByTestMenu(tmpMenu);

        linkList.forEach(tmp -> {
            TestLink testLink = new TestLink();
            testLink.setLink(tmp);
            testLink.setTestMenu(testMenu);
            testMenu.addLink(testLink);

//            testLinkRepository.save(testLink);
        });

//        testMenuRepository.save(testMenu);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test9(){
        Recipe recipe = recipeRepository.getRecipeByContentId(1L);
//        Recipe recipe = Recipe.builder()
//                .contentId(1L)
//                .build();
        System.out.println("-----------------------");
        List<Link> linkList = recipe.getLink();
        System.out.println("-----------------------");
//        RecipeDto dto = recipe.toDto();
        List<Link> newLinkList = linkRepository.findAllByRecipe(recipe);
        System.out.println("-----------------------");
        Recipe newRecipe = newLinkList.get(0).getRecipe();
    }
}
