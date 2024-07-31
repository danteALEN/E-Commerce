package com.example.ecommerce.controller;


import com.example.ecommerce.Models.signInModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthentication {

    @GetMapping
    public signInModel Signin(){
        return new signInModel();
    }
}
