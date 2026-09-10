package com.example.ms2_vuelos_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/api/vuelos/ping")
    public String ping() {
        return "MS2 vuelos-api funcionando";
    }
}
