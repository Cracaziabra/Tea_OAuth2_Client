package com.example.oauth2_client.controllers;

import com.example.oauth2_client.tea.Tea;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.ArgumentMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@WebMvcTest
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate rest;

    @Test
    @WithMockUser
    public void shouldReturnTea() throws Exception{
        when(rest.getForObject(anyString(), any())).thenReturn(new Tea[0]);
        this.mockMvc.perform(get("/tea"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tea"));
    }
}