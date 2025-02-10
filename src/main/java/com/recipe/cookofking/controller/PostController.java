package com.recipe.cookofking.controller;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.dto.post.PostViewDto;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    // 레시피 작성 폼
    @RequestMapping("/write")
    public String showWriteForm() {
        return "Post/post-write";
    }


    // 레시피 조회 폼
    @RequestMapping("/view/{postid}")
    public String showViewForm(@PathVariable(name = "postid") Integer postid, Model model) {
        PostViewDto postViewDto = postService.getPostById(postid);  // postid로 게시글 데이터 가져오기
        model.addAttribute("post", postViewDto);  // 모델에 데이터 추가
        return "post/post-view";  // post-view.html 렌더링
    }


    @GetMapping("/list")
    public String getPostList(Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "latest") String sort) {



        // 페이지 번호를 0부터 시작하도록 조정
        int adjustedPage = (page > 0) ? page - 1 : 0;

        // 정렬 기준 설정
        Sort sortOrder = getSortOrder(sort);
        PageRequest pageRequest = PageRequest.of(adjustedPage, size, sortOrder);

        // 서비스 호출
        Page<PostDto> postPage = postService.getPostList(pageRequest);

        List<PostDto> reversedList = new ArrayList<>(postPage.getContent());
        Collections.reverse(reversedList);


        // 모델에 데이터 추가
        model.addAttribute("postPage", postPage);
        model.addAttribute("totalPosts", postPage.getTotalElements());
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentPage", page);  // 현재 페이지를 모델에 추가 (1부터 시작)

        return "post/posts";  // posts.html로 렌더링
    }

    // 정렬 기준에 따라 Sort 객체 반환
    private Sort getSortOrder(String sort) {
        switch (sort) {
            case "views":
                return Sort.by(Sort.Direction.DESC, "viewCount");  // 조회수 기준 정렬
            case "likes":
                return Sort.by(Sort.Direction.DESC, "likeCount");  // 좋아요 기준 정렬
            default:
                return Sort.by(Sort.Direction.DESC, "createdDate");  // 기본값: 최신순 정렬
        }
    }

}
