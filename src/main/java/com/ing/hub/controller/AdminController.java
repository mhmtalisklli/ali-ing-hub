package com.ing.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ing.hub.service.CustomUserDetailsService;



@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	CustomUserDetailsService userService;

    @GetMapping("/hello")
    public String helloAdmin() {
        return "Merhaba Admin!";
    }
    
    

}
