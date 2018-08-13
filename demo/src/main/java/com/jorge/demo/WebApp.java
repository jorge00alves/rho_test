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
    
    Controller ctrl;
    
    public WebApp() {
        
        this.ctrl = new Controller();
        
    }
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping("/getA2B")
    public String getA2B(@RequestParam(value="a", defaultValue="EUR") String a, @RequestParam(value="b", defaultValue="USD") String b) {
        
        return ctrl.getA2B(a, b);
        
    }
    
    @RequestMapping("/getAllFromA")
    public String getAllFromA(@RequestParam(value="a", defaultValue="EUR") String a) {
        
        return ctrl.getAllFromA(a);
        
    }
    
    @RequestMapping("/convertA2B")
    public String convertA2B(@RequestParam(value="a", defaultValue="EUR") String a, @RequestParam(value="b", defaultValue="USD") String b, @RequestParam(value="c", defaultValue="25") float c) {
        
        return ctrl.convertA2B(a, b, c);
        
    }
    
    @RequestMapping("/convertA2SuppliedList")
    public String convertA2SuppliedList(@RequestParam(value="a", defaultValue="EUR") String a, @RequestParam(value="b", defaultValue="25") float b, @RequestParam(value="c", defaultValue="USD,AED") String c) {
        
        return ctrl.convertA2SuppliedList(a, b, c);
        
    }
    
}
