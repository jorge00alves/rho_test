/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorge.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.Document;

/**
 *
 * @author Joca
 */
public class DbQueries {
    
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;
    
    /**
     * Constructor. Instatiates database connection.
     */
    public DbQueries() {
        
        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://jorge:teste1@ds111012.mlab.com:11012/teste"));
        this.database = mongoClient.getDatabase("teste");
        this.collection = database.getCollection("Rates");
        
    }
    
    /**
     * Regular Mongodb document.
     * 
     * @param a - base currency
     * @param b - currency
     * @param c - currency rate
     * @param timestamp
     * @return 
     */
    public Document createDoc(String a, String b, double c, long timestamp) {
        
        Document doc = new Document("base", a).append("currency", b).append("rate", c).append("timestamp", timestamp);
        
        return doc;
        
    }
    
    /**
     * Method to insert Documents into database.
     * 
     * @param jo - argument containing the necessary fields to create Documents
     */
    public void putIntoMongoDB(JsonObject jo) {
        
        long timestamp = jo.get("timestamp").getAsLong();
        
        String a = jo.get("base").getAsString();
        
        List<Document> documents = new ArrayList<Document>(); //necessary to insert many documents
        
        for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) { //loop through rates

            String b = entry.getKey();
            
            double c = entry.getValue().getAsDouble();
            
            Document doc = createDoc(a, b, c, timestamp);
            
            documents.add(doc); //insert Document into ArrayList

        }
        
        this.collection.insertMany(documents); //insert documents into database
        
    }
    
    /**
     * Method to query database
     * 
     * @param jo - object containing the base currency
     * @param currencies - array with the currencies to create queries
     * @return 
     */
    public ArrayList<JsonObject> getFromMongoDB(JsonObject jo, String[] currencies) {
        
        ArrayList<JsonObject> ajo = new ArrayList<>();
        
        Block<Document> addToArrayList = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                
                for(int i=0; i<currencies.length; i++) {
            
                    if(document.get("currency").equals(currencies[i])) {
                        
                        JsonObject joToArrayList = new JsonObject();
                        joToArrayList.addProperty("base", jo.get("base").getAsString());
                        
                        joToArrayList.addProperty("currency", currencies[i]);
                        joToArrayList.addProperty("rate", document.getDouble("rate"));
                        joToArrayList.addProperty("timestamp", document.getLong("timestamp"));
                        
                        ajo.add(joToArrayList);
                        
                    }
            
                }
                
            }
        };
        
        this.collection.find(eq("base", jo.get("base").getAsString())).forEach(addToArrayList);
        
        return ajo;
        
    }
    
    /**
     * Method to update documents on database
     * 
     * @param jo - object containing base currency + timestamp + rate
     * @param b - currency
     */
    public void update(JsonObject jo, String b) {
        
        this.collection.updateOne(and(eq("base", jo.get("base").getAsString()), eq("currency", b)), combine(set("rate", jo.getAsJsonObject("rates").get(b).getAsDouble()), set("timestamp", jo.get("timestamp").getAsLong())));
        
    }
    
}
