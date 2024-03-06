package com.dp_ua.JogJourney.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@Slf4j
public class ErrorHandler implements ErrorController {

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception e) {
        log.info("Error: " + e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "errorPage";
    }

    @RequestMapping("/error")
    public String handleException(Model model, HttpServletRequest request) {
        String url = getRequestedURI();
        log.info("Error NOT FOUND: " + url);
        model.addAttribute("errorMessage", "Page not found: " + url);
        return "errorPage";
    }

    private static String getRequestedURI() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        String url = attributes.getRequest().getAttribute("jakarta.servlet.error.request_uri").toString();
        return url;
    }
}