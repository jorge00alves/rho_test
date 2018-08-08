/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorge.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joca
 */
public class DbQueries {
    
    public String fixerAccessKey = "d78c6330a71689fb613c11d334838ef1";
    
    public String[] currencies = new String[]{"EUR", "USD", "AUD", "CAD", "PLN", "MXN"};
    
    public DbQueries() {
        
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://jorge:teste1@ds111012.mlab.com:11012/teste"));
        DB database = mongoClient.getDB("teste");
        DBCollection collection = database.getCollection("Rates");
        
    }
    
    public String getA2B(String a, String b) {
        
        InputStream is = null;
        
        try {
            
            is = new URL("http://data.fixer.io/api/latest?access_key=" + fixerAccessKey + "&base=" + a + "&symbols=" + b).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            JsonObject jo = (new JsonParser()).parse(jsonText).getAsJsonObject();
            
            //System.out.println(jo.toString());
            
            if(jo.getAsJsonPrimitive("success").getAsBoolean()) {

                return "1 " + a + " = " + jo.getAsJsonObject("rates").get(b).toString() + " " + b;
                
            } else {
                
                if(jo.getAsJsonObject("error").get("info") != null) {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                    
                } else {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");
                    
                }
                
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
        
    }
    
    public String getAllFromA(String a) {
        
        InputStream is = null;
        
        try {
            
            ArrayList<String> als = new ArrayList<>();
            
            for(int i=0; i<currencies.length; i++) {
                
                if(!currencies[i].equals(a)) {
                    
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
            
            is = new URL("http://data.fixer.io/api/latest?access_key=" + fixerAccessKey + "&base=" + a + "&symbols=" + symbols).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            JsonObject jo = (new JsonParser()).parse(jsonText).getAsJsonObject();
            
            if(jo.getAsJsonPrimitive("success").getAsBoolean()) {
                
                String res = "";

                for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {

                    res += "1 " + a + " = " + entry.getValue().toString() + " " + entry.getKey() + "<br>";

                }

                return res;
                
            } else {
                
                if(jo.getAsJsonObject("error").get("info") != null) {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                    
                } else {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");
                    
                }
                
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
        
    }
    
    public String convertA2B(String a, String b, float c) {
        
        InputStream is = null;
        
        try {
            
            is = new URL("http://data.fixer.io/api/latest?access_key=" + fixerAccessKey + "&base=" + a + "&symbols=" + b).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            JsonObject jo = (new JsonParser()).parse(jsonText).getAsJsonObject();
            
            //System.out.println(jo.toString());
            
            if(jo.getAsJsonPrimitive("success").getAsBoolean()) {
                
                float res = jo.getAsJsonObject("rates").get(b).getAsFloat() * c;

                return c + " " + a + " = " + res + " " + b;
                
            } else {
                
                if(jo.getAsJsonObject("error").get("info") != null) {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                    
                } else {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");
                    
                }
                
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
        
    }
    
    public String convertAllFromA(String a, float b) {
        
        InputStream is = null;
        
        try {
            
            ArrayList<String> als = new ArrayList<>();
            
            for(int i=0; i<currencies.length; i++) {
                
                if(!currencies[i].equals(a)) {
                    
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
            
            is = new URL("http://data.fixer.io/api/latest?access_key=" + fixerAccessKey + "&base=" + a + "&symbols=" + symbols).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            JsonObject jo = (new JsonParser()).parse(jsonText).getAsJsonObject();
            
            if(jo.getAsJsonPrimitive("success").getAsBoolean()) {
                
                String res = "";

                for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {
                    
                    //float res = jo.getAsJsonObject("rates").get(b).getAsFloat() * c;
                    float conversion = entry.getValue().getAsFloat() * b;

                    res += b + " " + a + " = " + conversion + " " + entry.getKey() + "<br>";

                }

                return res;
                
            } else {
                
                if(jo.getAsJsonObject("error").get("info") != null) {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                    
                } else {
                    
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");
                    
                }
                
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
        
    }
    
    public String testeRates() {
        
        InputStream is = null;
        try {
            
            is = new URL("http://data.fixer.io/api/latest?access_key=" + fixerAccessKey + "&base=EUR&symbols=USD,AUD,CAD,PLN,MXN").openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            JsonObject jo = (new JsonParser()).parse(jsonText).getAsJsonObject();
            
            //System.out.println(jo.toString());
            return jo.getAsJsonObject("rates").get("USD").toString();
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DbQueries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
        
    }
    
}
