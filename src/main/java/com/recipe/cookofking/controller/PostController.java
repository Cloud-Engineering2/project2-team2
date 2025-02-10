package com.recipe.cookofking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {

    @RequestMapping("/")
    public String showForm() {
        return "Post/post";  // 레시피 작성 폼 렌더링
    }
}
