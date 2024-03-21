package com.dp_ua.JogJourney.web;

import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import com.dp_ua.JogJourney.strava.StravaFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class StravaController {
    @Autowired
    StravaFacade stravaFacade;

    @GetMapping("/strava/redirect")
    public ModelAndView handleStravaRedirect(@RequestParam("code") String code,
                                             @RequestParam("scope") String scope,
                                             @RequestParam("chatId") String chatId) {
        StravaAthlete stravaAthlete = operateUserCode(code, scope, chatId);
        if (stravaAthlete == null) {

            ModelAndView errorPage = new ModelAndView("errorPage");
            errorPage.addObject("error", "Error during Strava authentication. No athlete found");
            return errorPage;
        }
        ModelAndView mav = new ModelAndView("redirectPage");
        mav.addObject("code", code);
        mav.addObject("scope", scope);
        mav.addObject("athlete", stravaAthlete);
        return mav;
    }

    private StravaAthlete operateUserCode(String code, String scope, String chatId) {
        log.info("Strava redirect: code: " + code + " scope: " + scope + " chatId: " + chatId);
        return stravaFacade.operateStravaCode(code, chatId, scope);
    }
}