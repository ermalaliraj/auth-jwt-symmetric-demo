package com.ea.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return "Hello JWT!";
    }

    @GetMapping("/details")
    public String details(Principal principal) {
        return "principal: " + principal + " \nprincipal.getName(): " + principal.getName();
    }

}
