package com.example.oauth2_client.controllers;

import com.example.oauth2_client.tea.Tea;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class DemoController {

    private final RestTemplate rest;

    @Value("${resource-server-base-uri}")
    private String resourceServerUri;

    @ExceptionHandler(value = HttpClientErrorException.Forbidden.class)
    public ModelAndView accessException(Exception e) {
        return new ModelAndView("accessDenied");
    }

    @GetMapping("/tea/create")
    public String teaForm(Model model) {
        model.addAttribute("teaForm", new Tea());
        return "create";
    }

    @GetMapping("/tea")
    public String selectTea(Model model) {
        Tea[] teaList = rest.getForObject(resourceServerUri + "/tea/search/findAllBy", Tea[].class);
        model.addAttribute("teaList", teaList);
        return "tea";
    }

    @PostMapping(value = "/saveTea")
    public String saveTea(@ModelAttribute Tea tea) {
        rest.postForObject(resourceServerUri + "/saveTea", tea, Tea.class);
        return "redirect:/tea";
    }

    @GetMapping(value = "/tea/delete/{id}")
    public String deleteTea(@PathVariable Long id) {
        rest.delete(resourceServerUri + "/delete/" + id, Tea.class);
        return "redirect:/tea";
    }
}
