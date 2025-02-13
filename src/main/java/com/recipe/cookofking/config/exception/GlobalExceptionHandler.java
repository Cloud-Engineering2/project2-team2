package com.recipe.cookofking.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 모든 예외를 처리
    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String errorMessage = ex.getMessage() != null && !ex.getMessage().isEmpty()
                ? ex.getMessage()
                : "알 수 없는 오류가 발생했습니다.";

        if (uri.startsWith("/api/")) {
            return new ResponseEntity<>(Map.of("error", errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", errorMessage);  // 메시지 설정
            mav.setViewName("error");
            return mav;
        }
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri.startsWith("/api/")) {
            return new ResponseEntity<>(Map.of("error", "요청한 리소스를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", "요청하신 페이지 '" + uri + "'를 찾을 수 없습니다.");  // 명시적 메시지 추가
            mav.setViewName("error");
            return mav;
        }
    }



    // 권한 예외 처리 (AccessDeniedException)도 동일한 페이지로 리디렉션
    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri.startsWith("/api/")) {
            return new ResponseEntity<>(Map.of("error", "접근 권한이 없습니다."), HttpStatus.FORBIDDEN);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", "접근 권한이 없습니다.");  // 권한 관련 메시지
            mav.setViewName("error");  // 동일한 에러 페이지 사용
            return mav;
        }
    }
}
