/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorge.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Joca
 */
public class Controller {
    
    public String fixerAccessKey = "d78c6330a71689fb613c11d334838ef1";
    
    public String[] currencies = new String[]{
        "AED",
        "AFN",
        "ALL",
        "AMD",
        "ANG",
        "AOA",
        "ARS",
        "AUD",
        "AWG",
        "AZN",
        "BAM",
        "BBD",
        "BDT",
        "BGN",
        "BHD",
        "BIF",
        "BMD",
        "BND",
        "BOB",
        "BRL",
        "BSD",
        "BTC",
        "BTN",
        "BWP",
        "BYR",
        "BZD",
        "CAD",
        "CDF",
        "CHF",
        "CLF",
        "CLP",
        "CNY",
        "COP",
        "CRC",
        "CUC",
        "CUP",
        "CVE",
        "CZK",
        "DJF",
        "DKK",
        "DOP",
        "DZD",
        "EGP",
        "ERN",
        "ETB",
        "EUR",
        "FJD",
        "FKP",
        "GBP",
        "GEL",
        "GGP",
        "GHS",
        "GIP",
        "GMD",
        "GNF",
        "GTQ",
        "GYD",
        "HKD",
        "HNL",
        "HRK",
        "HTG",
        "HUF",
        "IDR",
        "ILS",
        "IMP",
        "INR",
        "IQD",
        "IRR",
        "ISK",
        "JEP",
        "JMD",
        "JOD",
        "JPY",
        "KES",
        "KGS",
        "KHR",
        "KMF",
        "KPW",
        "KRW",
        "KWD",
        "KYD",
        "KZT",
        "LAK",
        "LBP",
        "LKR",
        "LRD",
        "LSL",
        "LTL",
        "LVL",
        "LYD",
        "MAD",
        "MDL",
        "MGA",
        "MKD",
        "MMK",
        "MNT",
        "MOP",
        "MRO",
        "MUR",
        "MVR",
        "MWK",
        "MXN",
        "MYR",
        "MZN",
        "NAD",
        "NGN",
        "NIO",
        "NOK",
        "NPR",
        "NZD",
        "OMR",
        "PAB",
        "PEN",
        "PGK",
        "PHP",
        "PKR",
        "PLN",
        "PYG",
        "QAR",
        "RON",
        "RSD",
        "RUB",
        "RWF",
        "SAR",
        "SBD",
        "SCR",
        "SDG",
        "SEK",
        "SGD",
        "SHP",
        "SLL",
        "SOS",
        "SRD",
        "STD",
        "SVC",
        "SYP",
        "SZL",
        "THB",
        "TJS",
        "TMT",
        "TND",
        "TOP",
        "TRY",
        "TTD",
        "TWD",
        "TZS",
        "UAH",
        "UGX",
        "USD",
        "UYU",
        "UZS",
        "VEF",
        "VND",
        "VUV",
        "WST",
        "XAF",
        "XAG",
        "XAU",
        "XCD",
        "XDR",
        "XOF",
        "XPF",
        "YER",
        "ZAR",
        "ZMK",
        "ZMW",
        "ZWL"
    };
    
    DbQueries dbq;
    FixerIO fio;
    
    public Controller() {
        
        this.dbq = new DbQueries();
        this.fio = new FixerIO();
        
    }
    
    public String getA2B(String a, String b) {
        
        JsonObject jo2send2dbqueries = new JsonObject();
        
        jo2send2dbqueries.addProperty("base", a);
        
        ArrayList<JsonObject> ajo = dbq.getFromMongoDB(jo2send2dbqueries, new String[]{b});
        
        JsonObject jo = null;
        
        if(ajo.size() == 0) {
            
            jo = fio.fetchData(a, new String[]{b});
            
            if(!jo.getAsJsonPrimitive("success").getAsBoolean()) {
            
                if(jo.getAsJsonObject("error").get("info") != null) {
                
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                
                } else {

                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");

                }

            } else {
                
                dbq.putIntoMongoDB(jo);
                
                return "1 " + a + " = " + jo.getAsJsonObject("rates").get(b).getAsDouble() + " " + b;
                
            }
            
        } else {
            
            jo = ajo.get(0);
            
            long dbtimestamp = jo.get("timestamp").getAsLong();
            
            long now = System.currentTimeMillis()/1000;
            float nminutos = (now - jo.get("timestamp").getAsLong())/60f;
            
            System.out.println("nminutos: " + nminutos);
            
            if(nminutos >= 30) {
                
                jo = fio.fetchData(a, new String[]{b});
            
                if(!jo.getAsJsonPrimitive("success").getAsBoolean()) {

                    if(jo.getAsJsonObject("error").get("info") != null) {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");

                    } else {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");

                    }

                } else {
                    
                    if(jo.get("timestamp").getAsLong() != dbtimestamp) {
                        
                        dbq.update(jo, b);
                        
                    } //else {
                        
                        //System.out.println("FixerIO still hasn't updated rates");
                        
                    //}
                    
                    return "1 " + a + " = " + jo.getAsJsonObject("rates").get(b) + " " + b;
                    
                }
                
            } else {
                
                return "1 " + a + " = " + jo.get("rate") + " " + b;
                
            }
            
        }
        
    }
    
    public String getAllFromA(String a) {
        
        JsonObject jo2send2dbqueries = new JsonObject();
        
        jo2send2dbqueries.addProperty("base", a);
        
        ArrayList<JsonObject> ajo = dbq.getFromMongoDB(jo2send2dbqueries, this.currencies);
        
        JsonObject jo = null;
        
        if(ajo.size() == 0) {
            
            jo = fio.fetchData(a, this.currencies);
            
            if(!jo.getAsJsonPrimitive("success").getAsBoolean()) {
            
                if(jo.getAsJsonObject("error").get("info") != null) {
                
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                
                } else {

                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");

                }

            } else {
                
                dbq.putIntoMongoDB(jo);
                
                String res = "";
            
                for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {

                    res += "1 " + a + " = " + entry.getValue() + " " + entry.getKey() + "<br>";

                }

                return res;
                
            }
            
        } else {
            
            boolean bool = false;
            
            ArrayList<String> straux = new ArrayList<>();
            
            for(JsonObject fjo: ajo) {
                
                long now = System.currentTimeMillis()/1000;
                float nminutos = (now - fjo.get("timestamp").getAsLong())/60f;
                
                if(nminutos >= 30) {
                    
                    bool = true;
                    
                }
                
                System.out.println("fjo:");
                System.out.println(fjo);
                
                straux.add(fjo.get("currency").getAsString());
                
            }
            
            if(!bool || straux.size() == this.currencies.length) {
                
                for(int i=0; i<this.currencies.length; i++) {
                    
                    if(!straux.contains(this.currencies[i])) {
                        
                        bool = true;
                        break;
                        
                    }
                    
                }
                
                if(!bool) {
                    
                    String res = "";
                    
                    for(JsonObject fjo2: ajo) {
                    
                        res += "1 " + a + " = " + fjo2.get("rate").getAsDouble() + " " + fjo2.get("currency").getAsString() + "<br>";

                    }
                    
                    return res;
                    
                }
                
            }
            
            if(bool) {
                
                jo = fio.fetchData(a, this.currencies);
                
                if(!jo.getAsJsonPrimitive("success").getAsBoolean()) {

                    if(jo.getAsJsonObject("error").get("info") != null) {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");

                    } else {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");

                    }

                } else { //falta inserir se não existir. usar o ajo
                    
                    String res = "";
                    
                    JsonObject json2database = new JsonObject();
                    
                    json2database.addProperty("base", jo.get("base").getAsString());
                    json2database.addProperty("timestamp", jo.get("timestamp").getAsLong());
                    
                    JsonObject innerjson2database = new JsonObject();
            
                    for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {
                        
                        if(straux.contains(entry.getKey())) {
                            
                            dbq.update(jo, entry.getKey());
                            
                            //jo.getAsJsonObject("rates").remove(entry.getKey());
                            
                        } else {
                            
                            innerjson2database.addProperty(entry.getKey(), entry.getValue().getAsDouble());
                            
                        }

                        res += "1 " + a + " = " + entry.getValue().toString() + " " + entry.getKey() + "<br>";

                    }
                    
                    json2database.add("rates", innerjson2database);
                    
                    if(json2database.getAsJsonObject("rates").size() != 0) {
                        
                        dbq.putIntoMongoDB(jo);
                        
                    }

                    return res;
                    
                }
                
            }
            
            return null;
            
        }
        
    }
    
    public String convertA2B(String a, String b, double c) {
        
        InputStream is = null;
        
        JsonObject jo = fio.fetchData(a, new String[]{b});
        if(jo.getAsJsonPrimitive("success").getAsBoolean()) {
            
            double res = jo.getAsJsonObject("rates").get(b).getAsDouble() * c;
            
            return c + " " + a + " = " + res + " " + b;
            
        } else {
            
            if(jo.getAsJsonObject("error").get("info") != null) {
                
                return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                
            } else {
                
                return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");
                
            }
            
        }
        
    }
    
    /**
     * Isto está mal. Falta mais um argumento array de strings
     * 
     * @param a
     * @param b
     * @return 
     */
    public String convertA2SuppliedList(String a, double b, String currencies) {
        
        String[] curr = currencies.split(",");
        
        JsonObject jo = fio.fetchData(a, curr);
        
        if(jo.getAsJsonPrimitive("success").getAsBoolean()) {
            
            String res = "";
            
            for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {
                
                //float res = jo.getAsJsonObject("rates").get(b).getAsFloat() * c;
                double conversion = entry.getValue().getAsDouble() * b;
                
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
        
    }
    
}
