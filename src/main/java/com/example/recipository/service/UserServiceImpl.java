package com.example.recipository.service;

import com.example.recipository.domain.*;
import com.example.recipository.dto.UserDto;
import com.example.recipository.repository.CommentRepository;
import com.example.recipository.repository.LinkRepository;
import com.example.recipository.repository.RecipeRepository;
import com.example.recipository.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final LinkRepository linkRepository;
    private final CommentRepository commentRepository;

    public UserServiceImpl(UserRepository userRepository, RecipeRepository recipeRepository, LinkRepository linkRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.linkRepository = linkRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public boolean duplCheck(UserDto userDto) {
        boolean isChecked = false;

        if(userDto.getEmail() != null) {
            isChecked = userRepository.existsByEmail(userDto.getEmail());
        } else if(userDto.getName() != null) {
            isChecked = userRepository.existsByName(userDto.getName());
        }

        return isChecked;
    }

    @Transactional
    @Override
    public boolean signin(UserDto userDto) {
        try{
            // 비밀번호 encoding
            Member member = userDto.toEntity();

            member.addAuthority();
            userRepository.save(member);

            return true;
        } catch (Exception e){
            return false;
        }
    }

//    @Transactional
//    // 권한을 추가하는 method
//    public void addAuthority(Long userId, String authority){
//        userRepository.findById(userId).ifPresent(user -> {
//            SpAuthority spAuthority = new SpAuthority(user.getUserId(), authority);
//            if(user.getAuthorities() == null){
//                HashSet<SpAuthority> authorities = new HashSet<>();
//                authorities.add(spAuthority);
//                user.setAuthorities(authorities);
//                userRepository.save(user);
//            } else if(!user.getAuthorities().contains(spAuthority)){
//                HashSet<SpAuthority> authorities = new HashSet<>();
//                authorities.addAll(user.getAuthorities());
//                authorities.add(spAuthority);
//                user.setAuthorities(authorities);
//                userRepository.save(user);
//            }
//        });
//    }

//    @Override
//    public boolean login(User user) {
//        SpUser dbData = userRepository.getByEmail(user.getEmail());
//
//        // DB에 data가 있는 경우
//        if(dbData != null) {
//            boolean checkPwd = BCrypt.checkpw(user.getPassword(), dbData.getPassword());
//
//            // 비밀 번호가 일치하는 경우
//            if(checkPwd){
//                return true;
//            }
//        }
//
//        return false;
//    }

    // 유저의 정보를 가져오는 method
    @Override
    public UserDto getProfile(String email) {
        Member user = userRepository.getMemberByEmail(email);

        return user.toDto();
    }

    // 사용자의 프로필 정보를 수정하는 service logic
    @Transactional
    @Override
    public boolean updateProfile(UserDto userDto) {
        try {
            // Authentication Principal의 email data를 기반으로 DB에서 사용자 정보를 가져옴
            Member user = userRepository.getMemberByEmail(userDto.getEmail());

//            // 게시글 writer 변경
//            // 작성자 정보로 작성한 게시글 정보를 가져와서
//            List<Recipe> recipeList = recipeRepository.getAllByWriter(user.getName());
//            // 각각에 대해 작성자 정보를 update하고
//            recipeList.forEach(tmp -> {
//                tmp.updateWriter(userDto.getName());
//            });
//            // save (update query)
//            recipeRepository.saveAll(recipeList);

//            // 댓글 writer 변경
//            // 작성자 정보로 작성한 댓글 정보를 가져와서
//            List<Comment> commentList = commentRepository.getAllByWriter(user.getName());
//            // 각각에 대해 작성자 정보를 update하고
//            commentList.forEach(tmp -> {
//                tmp.updateWriter(userDto.getName());
//            });
//            // save (update query)
//            commentRepository.saveAll(commentList);

            // 사용자 정보 또한 update 하고 save
            user.updateName(userDto);
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 사용자의 비밀번호를 수정하는 service logic
    @Override
    public boolean updatePassword(UserDto userDto) {
        try {
            // Authentication Principal의 email data를 기반으로 DB에서 사용자 정보를 가져옴
            Member user = userRepository.getMemberByEmail(userDto.getEmail());
            String dbPassword = user.getPassword();
            String oldPassword = userDto.getOldPassword();
            // DB data와 입력한 data의 일치 여부에 따라
            boolean beMatched = BCrypt.checkpw(oldPassword, dbPassword);

            // 일치하는 경우
            if(beMatched){
                // 새로 입력한 password를 encoding 해서
                String newPassword = userDto.getPassword();
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodedPwd = encoder.encode(newPassword);

                // 사용자 정보를 update 하고 save
                user.updatePassword(encodedPwd);
                userRepository.save(user);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // 회원 탈퇴 service logic
    @Transactional
    @Override
    public boolean exit(Member member) {
        try {
            // 온전한 Member (Recipe List, Comment List 보유)
            member = userRepository.getMemberByEmail(member.getEmail());
            // table에 임의로 만들어 둔 0번 사용자의 정보. 탈퇴한 회원임을 name으로 한다.
            Member deleteMember = userRepository.getReferenceById(0L);

            // 삭제하고자 하는 게시글 목록
            List<Recipe> recipeList = recipeRepository.getAllByMember(member);

            // 모든 게시글에 있는 사용자의 댓글 작성자를 0번으로 변경
            commentRepository.updateAllByMember(member, deleteMember);

            if(recipeList.size() != 0){
                // 사용자가 작성한 게시글 하위에 있는 모든 댓글 삭제
                commentRepository.deleteAllByRecipes(recipeList);
                // 사용자가 작성한 게시글의 참조 링크 삭제
                linkRepository.deleteAllByRecipes(recipeList);
            }
            // 사용자가 작성한 게시글 삭제
            recipeRepository.deleteAllByMember(member);

            // 사용자의 정보 삭제 (작성한 게시글과 댓글 삭제)
            userRepository.delete(member);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
