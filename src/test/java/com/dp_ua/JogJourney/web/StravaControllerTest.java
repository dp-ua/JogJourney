package com.dp_ua.JogJourney.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class StravaControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private StravaController stravaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stravaController).build();
    }

    @Test
    public void testHandleStravaRedirect() throws Exception {
        mockMvc.perform(get("/strava/redirect").param("code", "1234").param("scope", "read"))
                .andExpect(status().isOk())
                .andExpect(view().name("redirectPage"))
                .andExpect(model().attribute("code", "1234"));
    }
}