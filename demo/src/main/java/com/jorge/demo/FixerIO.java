/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorge.demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joca
 */
public class FixerIO {
    
    public String fixerAccessKey = "d78c6330a71689fb613c11d334838ef1";
    
    public FixerIO() {
        
    }
    
    public JsonObject fetchData(String base, String[] currencies) {
        
        InputStream is = null;
        
        try {
            
            ArrayList<String> als = new ArrayList<>();
            
            for(int i=0; i<currencies.length; i++) {
                
                if(!currencies[i].equals(base)) {
                    
                    als.add(currencies[i]);
                    
                }
                
            }
            
            String symbols = "";
            
            for(int n=0; n<als.size(); n++) {
                
                symbols += als.get(n);
                if(n != als.size()-1) {
                    symbols += ",";
                }
                
            }
            
            is = new URL("http://data.fixer.io/api/latest?access_key=" + this.fixerAccessKey + "&base=" + base + "&symbols=" + symbols).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            JsonObject jo = (new JsonParser()).parse(jsonText).getAsJsonObject();
            
            return jo;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(FixerIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FixerIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(FixerIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
        
    }
    
}
