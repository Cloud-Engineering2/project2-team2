package com.recipe.cookofking.controller;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

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
    public String showViewForm(@PathVariable Integer postid, Model model) {
        PostDto postDto = postService.getPostById(postid);  // postid로 게시글 데이터 가져오기
        model.addAttribute("post", postDto);  // 모델에 데이터 추가
        return "Post/post-view";  // post-view.html 렌더링
    }

}
