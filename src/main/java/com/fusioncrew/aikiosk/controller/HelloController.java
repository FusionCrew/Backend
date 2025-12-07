package com.fusioncrew.aikiosk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'><title>AI Kiosk Backend</title>" +
                "<style>body{font-family:system-ui;display:flex;justify-content:center;align-items:center;" +
                "height:100vh;margin:0;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:#fff;}" +
                "div{text-align:center;}h1{font-size:3rem;margin-bottom:1rem;}p{font-size:1.2rem;opacity:0.9;}</style></head>"
                +
                "<body><div><h1>🍔 AI Kiosk Backend</h1><p>Hello World! Spring Boot Server is running.</p>" +
                "<p style='margin-top:2rem;font-size:0.9rem;opacity:0.7'>FusionCrew © 2024</p></div></body></html>";
    }

    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"ok\"}";
    }
}
