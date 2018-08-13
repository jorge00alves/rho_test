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
    
    public DbQueries() {
        
        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://jorge:teste1@ds111012.mlab.com:11012/teste"));
        this.database = mongoClient.getDatabase("teste");
        this.collection = database.getCollection("Rates");
        
    }
    
    public Document createDoc(String a, String b, double c, long timestamp) {
        
        Document doc = new Document("base", a).append("currency", b).append("rate", c).append("timestamp", timestamp);
        
        return doc;
        
    }
    
    /**
     * Alterar de maneira a receber um JsonObject com 4 campos (base + currency + rate + timestamp).
     * Ou então isto pode estar assim.
     * 
     * @param jo 
     */
    public void putIntoMongoDB(JsonObject jo) {
        
        long timestamp = jo.get("timestamp").getAsLong();
        
        String a = jo.get("base").getAsString();
        
        List<Document> documents = new ArrayList<Document>();
        
        for (Map.Entry<String,JsonElement> entry : jo.getAsJsonObject("rates").entrySet()) {

            //res += "1 " + a + " = " + entry.getValue().toString() + " " + entry.getKey() + "<br>";
            String b = entry.getKey();
            
            double c = entry.getValue().getAsDouble();
            
            Document doc = createDoc(a, b, c, timestamp);
            
            documents.add(doc);
            
            //this.collection.insertOne(doc);

        }
        
        this.collection.insertMany(documents);
        
    }
    
    /**
     * Fazer um arraylist de JsonObjects. Cada JsonObject tem os 4 campos (base + currency + rate + timestamp). Isto vai fazer com que altere o código todo no FixerIO.
     * 
     * @param jo
     * @param currencies
     * @return 
     */
    public ArrayList<JsonObject> getFromMongoDB(JsonObject jo, String[] currencies) {
        
        ArrayList<JsonObject> ajo = new ArrayList<>();
        
        Block<Document> add2arraylist = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                //System.out.println(document.toJson());
                
                for(int i=0; i<currencies.length; i++) {
                    
                    //System.out.println("cursor.next().get(\"currency\"): " + cursor.next().get("currency"));
                    //System.out.println("currencies: " + currencies);
            
                    if(document.get("currency").equals(currencies[i])) {
                        
                        JsonObject jo2arraylist = new JsonObject();
                        jo2arraylist.addProperty("base", jo.get("base").getAsString());
                        
                        jo2arraylist.addProperty("currency", currencies[i]);
                        jo2arraylist.addProperty("rate", document.getDouble("rate"));
                        jo2arraylist.addProperty("timestamp", document.getLong("timestamp"));
                        
                        ajo.add(jo2arraylist);
                        
                    }
            
                }
                
            }
        };
        
        this.collection.find(eq("base", jo.get("base").getAsString())).forEach(add2arraylist);
        
        return ajo;
        
    }
    
    public void update(JsonObject jo, String b) {
        
        //System.out.println(jo);
        
        this.collection.updateOne(and(eq("base", jo.get("base").getAsString()), eq("currency", b)), combine(set("rate", jo.getAsJsonObject("rates").get(b).getAsDouble()), set("timestamp", jo.get("timestamp").getAsLong())));
        
    }
    
}
