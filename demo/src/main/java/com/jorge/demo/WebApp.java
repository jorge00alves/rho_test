/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorge.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Joca
 */
@RestController
public class WebApp {
    
    DbQueries dbq;
    
    public WebApp() {
        
        this.dbq = new DbQueries();
        
    }
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping("/getA2B")
    public String getA2B(@RequestParam(value="a", defaultValue="EUR") String a, @RequestParam(value="b", defaultValue="USD") String b) {
        
        return dbq.getA2B(a, b);
        
    }
    
    @RequestMapping("/getAllFromA")
    public String getAllFromA(@RequestParam(value="a", defaultValue="EUR") String a) {
        
        return dbq.getAllFromA(a);
        
    }
    
    @RequestMapping("/convertA2B")
    public String convertA2B(@RequestParam(value="a", defaultValue="EUR") String a, @RequestParam(value="b", defaultValue="USD") String b, @RequestParam(value="c", defaultValue="1") float c) {
        
        return dbq.convertA2B(a, b, c);
        
    }
    
    @RequestMapping("/convertAllFromA")
    public String convertAllFromA(@RequestParam(value="a", defaultValue="EUR") String a, @RequestParam(value="b", defaultValue="1") float b) {
        
        return dbq.convertAllFromA(a, b);
        
    }
    
    @RequestMapping("/rate")
    public String rate() {
        
        return dbq.testeRates();
        
    }
    
}
