package com.example.recipository.service;

import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.RecipeDto;
import com.example.recipository.repository.LinkRepository;
import com.example.recipository.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final LinkRepository linkRepository;

    @Value("${file.directory}")
    private String savePath;

    public RecipeServiceImpl(RecipeRepository recipeRepository, LinkRepository linkRepository) {
        this.recipeRepository = recipeRepository;
        this.linkRepository = linkRepository;
    }

    // 게시글을 작성하는 service logic
    @Transactional
    @Override
    public boolean write(RecipeDto recipeDto,
                         MultipartFile imageFile,
                         String username) {
        try {
            if (!imageFile.isEmpty()) {
                // application.properties 에 작성한 save path
                // 각 운영체제에 맞는 separator
                String savePath = this.savePath + File.separator;
                File file = new File(savePath);
                // 해당 경로에 directory가 없을 시 만듦
                if (!file.exists()) {
                    file.mkdir();
                }

                // 넘겨받은 file name
                String originFileName = imageFile.getOriginalFilename();
                // save file name에 사용할 UUID String
                String uuid = UUID.randomUUID().toString();
                // save file name
                String saveFileName = uuid + originFileName;

                try {
                    // directory에 upload file save
                    imageFile.transferTo(new File(savePath + saveFileName));
                    // RecipeDto에 image save path setting
                    recipeDto.setImagePath(savePath + saveFileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // RecipeDto에 SpUser의 username setting
            recipeDto.setWriter(username);
            // RecipeDto에 조회수, 좋아요 0으로 setting
            recipeDto.setViewCount(0L);
            recipeDto.setLikeCount(0L);

            // RecipeDto를 Entity로 전환하고
            Recipe recipe = recipeDto.toEntity();
            // Entity의 List<Link> 의 각 Link에 Recipe setting
            recipe.setRecipeAtLink();

            // 각각 save
            recipeRepository.save(recipe);
            linkRepository.saveAll(recipe.getLink());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
