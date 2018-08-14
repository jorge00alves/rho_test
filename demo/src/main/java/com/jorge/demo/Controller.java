/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorge.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        
        JsonObject joToSendToDbQueries = new JsonObject();
        
        joToSendToDbQueries.addProperty("base", a);
        
        ArrayList<JsonObject> ajo = dbq.getFromMongoDB(joToSendToDbQueries, new String[]{b});
        
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
            
            //System.out.println("nMinutos: " + nMinutos);
            
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
        
        JsonObject joToSendToDbQueries = new JsonObject();
        
        joToSendToDbQueries.addProperty("base", a);
        
        ArrayList<JsonObject> ajo = dbq.getFromMongoDB(joToSendToDbQueries, this.currencies);
        
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
            
            ArrayList<String> strAux = new ArrayList<>();
            
            for(JsonObject fjo: ajo) {
                
                long now = System.currentTimeMillis()/1000;
                float nMinutos = (now - fjo.get("timestamp").getAsLong())/60f;
                
                if(nMinutos >= 30) {
                    
                    bool = true;
                    
                }
                
                //System.out.println("fjo:");
                //System.out.println(fjo);
                
                strAux.add(fjo.get("currency").getAsString());
                
            }
            
            if(!bool) { // || strAux.size() == this.currencies.length
                
                for(int i=0; i<this.currencies.length; i++) {
                    
                    if(!strAux.contains(this.currencies[i])) {
                        
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

                } else {
                    
                    String res = "";
                    
                    JsonObject jsonToDatabase = new JsonObject();
                    
                    jsonToDatabase.addProperty("base", jo.get("base").getAsString());
                    jsonToDatabase.addProperty("timestamp", jo.get("timestamp").getAsLong());
                    
                    JsonObject innerJsonToDatabase = new JsonObject();
            
                    for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {
                        
                        if(strAux.contains(entry.getKey())) {
                            
                            //System.out.println("key:");
                            //System.out.println(entry.getKey());
                            
                            dbq.update(jo, entry.getKey());
                            
                        } else {
                            
                            innerJsonToDatabase.addProperty(entry.getKey(), entry.getValue().getAsDouble());
                            
                        }

                        res += "1 " + a + " = " + entry.getValue().toString() + " " + entry.getKey() + "<br>";

                    }
                    
                    jsonToDatabase.add("rates", innerJsonToDatabase);
                    
                    if(jsonToDatabase.getAsJsonObject("rates").size() != 0) {
                        
                        dbq.putIntoMongoDB(jsonToDatabase);
                        
                    }

                    return res;
                    
                }
                
            }
            
            return null;
            
        }
        
    }
    
    public String convertA2B(String a, String b, double c) {
        
        JsonObject joToSendToDbQueries = new JsonObject();
        
        joToSendToDbQueries.addProperty("base", a);
        
        ArrayList<JsonObject> ajo = dbq.getFromMongoDB(joToSendToDbQueries, new String[]{b});
        
        JsonObject jo = null;
        
        //System.out.println("From db:");
        //System.out.println(ajo.toString());
        
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
                
                //System.out.println("jo:");
                //System.out.println(jo);
                
                double res = jo.getAsJsonObject("rates").get(b).getAsDouble() * c;
            
                return c + " " + a + " = " + res + " " + b;
                
            }
            
        } else {
            
            jo = ajo.get(0);
            
            long dbTimestamp = jo.get("timestamp").getAsLong();
            
            long now = System.currentTimeMillis()/1000;
            float nMinutos = (now - jo.get("timestamp").getAsLong())/60f;
            
            //System.out.println("nMinutos: " + nMinutos);
            
            if(nMinutos >= 30) {
                
                jo = fio.fetchData(a, new String[]{b});
            
                if(!jo.getAsJsonPrimitive("success").getAsBoolean()) {

                    if(jo.getAsJsonObject("error").get("info") != null) {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");

                    } else {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");

                    }

                } else {
                    
                    if(jo.get("timestamp").getAsLong() != dbTimestamp) {
                        
                        dbq.update(jo, b);
                        
                    } //else {
                        
                        //System.out.println("FixerIO still hasn't updated rates");
                        
                    //}
                    
                    double res = jo.getAsJsonObject("rates").get(b).getAsDouble() * c;
            
                    return c + " " + a + " = " + res + " " + b;
                    
                }
                
            } else {
                
                double res = jo.get("rate").getAsDouble() * c;
                
                return c + " " + a + " = " + res + " " + b;
                
            }
            
        }
        
    }
    
    public String convertA2SuppliedList(String a, String currencies , double c) {
        
        String[] curr = currencies.split(",");
        
        JsonObject joToSendToDbQueries = new JsonObject();
        
        joToSendToDbQueries.addProperty("base", a);
        
        ArrayList<JsonObject> ajo = dbq.getFromMongoDB(joToSendToDbQueries, curr);
        
        JsonObject jo = null;
        
        //System.out.println("From db:");
        //System.out.println(ajo.toString());
        
        if(ajo.size() == 0) {
            
            jo = fio.fetchData(a, curr);
            
            if(!jo.getAsJsonPrimitive("success").getAsBoolean()) {
            
                if(jo.getAsJsonObject("error").get("info") != null) {
                
                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");
                
                } else {

                    return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");

                }

            } else {
                
                dbq.putIntoMongoDB(jo);
                
                //System.out.println("jo:");
                //System.out.println(jo);
                
                String res = "";
            
                for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {

                    //float res = jo.getAsJsonObject("rates").get(b).getAsFloat() * c;
                    double conversion = entry.getValue().getAsDouble() * c;

                    res += c + " " + a + " = " + conversion + " " + entry.getKey() + "<br>";

                }

                return res;
                
            }
            
        } else {
            
            boolean bool = false;
            
            ArrayList<String> straux = new ArrayList<>();
            
            for(JsonObject fjo: ajo) {
                
                long now = System.currentTimeMillis()/1000;
                float nMinutos = (now - fjo.get("timestamp").getAsLong())/60f;
                
                if(nMinutos >= 30) {
                    
                    bool = true;
                    
                }
                
                //System.out.println("fjo:");
                //System.out.println(fjo);
                
                straux.add(fjo.get("currency").getAsString());
                
            }
            
            if(!bool) {
                
                for(int i=0; i<curr.length; i++) {
                    
                    if(!straux.contains(curr[i])) {
                        
                        bool = true;
                        break;
                        
                    }
                    
                }
                
                if(!bool) {
                    
                    String res = "";
                    
                    for(JsonObject fjo2: ajo) {
                    
                        res += c + " " + a + " = " + fjo2.get("rate").getAsDouble() * c + " " + fjo2.get("currency").getAsString() + "<br>";

                    }
                    
                    return res;
                    
                }
                
            }
            
            if(bool) {
                
                jo = fio.fetchData(a, curr);
                
                if(!jo.getAsJsonPrimitive("success").getAsBoolean()) {

                    if(jo.getAsJsonObject("error").get("info") != null) {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type") + "<br>" + "Info: " + jo.getAsJsonObject("error").get("info");

                    } else {

                        return "Error <br>" + "Code: " + jo.getAsJsonObject("error").get("code") + "<br>" + "Type: " + jo.getAsJsonObject("error").get("type");

                    }

                } else {
                    
                    String res = "";
                    
                    JsonObject jsonToDatabase = new JsonObject();
                    
                    jsonToDatabase.addProperty("base", jo.get("base").getAsString());
                    jsonToDatabase.addProperty("timestamp", jo.get("timestamp").getAsLong());
                    
                    JsonObject innerJsonToDatabase = new JsonObject();
            
                    for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {
                        
                        if(straux.contains(entry.getKey())) {
                            
                            dbq.update(jo, entry.getKey());
                            
                            //jo.getAsJsonObject("rates").remove(entry.getKey());
                            
                        } else {
                            
                            innerJsonToDatabase.addProperty(entry.getKey(), entry.getValue().getAsDouble());
                            
                        }

                        double conversion = entry.getValue().getAsDouble() * c;

                        res += c + " " + a + " = " + conversion + " " + entry.getKey() + "<br>";

                    }
                    
                    jsonToDatabase.add("rates", innerJsonToDatabase);
                    
                    if(jsonToDatabase.getAsJsonObject("rates").size() != 0) {
                        
                        dbq.putIntoMongoDB(jsonToDatabase);
                        
                    }

                    return res;
                    
                }
                
            }
            
            return null;
            
        }
        
    }
    
}
