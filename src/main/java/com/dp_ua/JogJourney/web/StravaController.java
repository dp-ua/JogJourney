package com.dp_ua.JogJourney.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class StravaController {

    @GetMapping("/strava/redirect")
    public ModelAndView handleStravaRedirect(@RequestParam("code") String code,
                                             @RequestParam("scope") String scope,
                                             HttpServletRequest request) {
        operateUserCode(code, scope);
        ModelAndView mav = new ModelAndView("redirectPage");
        mav.addObject("code", code);
        return mav;
    }

    private void operateUserCode(String code, String scope) {
        log.info("Strava redirect: code: " + code + " scope: " + scope);
    }
}