package com.recipe.cookofking.controller;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @RequestMapping("/")
    public String showForm() {
        return "Post/post";  // 레시피 작성 폼 렌더링
    }

    @GetMapping("/recipe/{postId}/update")
    public String update(@PathVariable Integer postId, Model model) {
        PostDto postDto = postService.getPostById(postId);
        log.debug(postDto.toString());
        model.addAttribute("post", postDto);

        return "Post/post";
    }

}
