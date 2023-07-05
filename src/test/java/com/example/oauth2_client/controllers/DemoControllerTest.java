package com.example.oauth2_client.controllers;

import com.example.oauth2_client.tea.Tea;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate rest;

    @Test
    @WithMockUser
    public void shouldReturnTeaWhenAuthorized() throws Exception{
        when(rest.getForObject(anyString(), any())).thenReturn(new Tea[0]);
        this.mockMvc.perform(get("/tea"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tea"));
    }

    @Test
    public void shouldntReturnTeaWhenUnauthorized() throws Exception{
        when(rest.getForObject(anyString(), any())).thenReturn(new Tea[0]);
        this.mockMvc.perform(get("/tea"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void shouldAddTeaFormToModel() throws Exception {
        this.mockMvc.perform(get("/tea/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeExists("teaForm"));
    }

    @Test
    public void shouldntAddTeaFormWhenUnauthorized() throws Exception {
        this.mockMvc.perform(get("/tea/create"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void shouldPostTeaAndRedirect() throws Exception {
        Tea testTea = new Tea();
        testTea.setName("testTea");
        testTea.setOrigin("Testland");
        this.mockMvc.perform(post("/saveTea")
                        .flashAttr("tea", testTea)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/tea"));

        verify(rest).postForObject(anyString(), same(testTea), any());
    }

    @Test
    public void shouldntPostTeaWhenUnauthorized() throws Exception {
        Tea testTea = new Tea();
        testTea.setName("testTea");
        testTea.setOrigin("Testland");
        this.mockMvc.perform(post("/saveTea")
                        .flashAttr("tea", testTea)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(rest, never()).postForObject(anyString(), same(testTea), any());
    }

    @Test
    @WithMockUser
    public void shouldDeleteAndRedirect() throws Exception {
        this.mockMvc.perform(get("/tea/delete/{id}", 1))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/tea"));

        verify(rest).delete(contains("/delete/1"), any(Class.class));
    }

    @Test
    public void shouldntDeleteWhenUnauthorized() throws Exception {
        this.mockMvc.perform(get("/tea/delete/{id}", 1))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(rest, never()).delete(anyString(), any(Class.class));
    }

}