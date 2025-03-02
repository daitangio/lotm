package com.gioorgi.lotm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoApi {

    @GetMapping("/api/info")
    public String version(){
        return "1.0-Coros";
    }
}
