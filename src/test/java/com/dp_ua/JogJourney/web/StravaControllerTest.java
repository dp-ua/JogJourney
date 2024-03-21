package com.dp_ua.JogJourney.web;

import com.dp_ua.JogJourney.strava.StravaFacade;
import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StravaControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private StravaController stravaController;

    @Mock
    private StravaFacade stravaFacade;

    @BeforeEach
    public void setup() {
        initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(stravaController)
                .build();
    }

    @Test
    public void testHandleStravaRedirect() throws Exception {
        prepareMockOperateStravaCode();
        mockMvc.perform(get("/strava/redirect")
                        .param("code", "1234")
                        .param("scope", "read")
                        .param("chatId", "4321")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("redirectPage"))
                .andExpect(model().attribute("code", "1234"))
                .andExpect(model().attribute("scope", "read"));
    }

    private void prepareMockOperateStravaCode() {
        when(stravaFacade.operateStravaCode("1234", "4321", "read"))
                .thenReturn(new StravaAthlete());
    }
}