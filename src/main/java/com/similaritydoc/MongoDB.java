/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import static com.similaritydoc.textrazor.TextRazor.analyseText;
import com.similaritydoc.textrazor.TextRazorObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import java.util.*;
import java.io.*;

/**
 *
 * @author DM
 */
public class MongoDB {
    
    MongoClient mongoClient;    
    com.mongodb.client.MongoDatabase database; 
    
    public MongoDB(String adress, int port) {
    	mongoClient = new MongoClient(adress, port);
    	database = mongoClient.getDatabase("Proba");
    }
    
    public void updateDocuments() throws NetworkException, AnalysisException{
        
              String API_KEY = "";
              Properties prop = new Properties();
	      InputStream input = null;
              TextRazorObject tro = null;

        
        	try {
                
                    input = new FileInputStream("src/main/resources/config.properties");

		// load a properties file
		prop.load(input);
                API_KEY =  prop.getProperty("API_KEY"); 
                
                } catch (IOException ex) {
		ex.printStackTrace();
	        } 
        
        FindIterable<Document> find = database.getCollection("proba2").find();
        
        for (Document document : find) {
            
           tro = analyseText(API_KEY, document.getString("Content"));
           BasicDBList entities = new BasicDBList();
           BasicDBList topics = new BasicDBList();
           
           for (int i =0; i<tro.entities.size(); i++){
           entities.add(tro.entities.get(i));
           }
           for (int j=0; j<tro.topics.size(); j++){
           topics.add(tro.topics.get(j));
           }
          
		database.getCollection("proba2").updateOne(new Document("Title", document.getString("Title")),new Document("$set", new Document("entities", entities)));
                database.getCollection("proba2").updateOne(new Document("Title", document.getString("Title")),new Document("$set", new Document("topics", topics)));

	}			
    }
    
    public List<MainDocument> getDocuments(){
       
        FindIterable<Document> find = database.getCollection("proba2").find().limit(20);
        List<MainDocument> documents = new ArrayList<>();
        int num = 1;
 
            for (Document document : find) {
            
                        MainDocument doc = null;
		        doc = new MainDocument(num, document.getString("Title"), document.getString("Content"), 0 , (ArrayList) document.get("entities"), (ArrayList) document.get("topics"));
			documents.add(doc);
			num++;        
	    }
        return documents;
    }
   
    public List<MainDocument> getSearchResult(String name) throws IOException {
		
                FindIterable<Document> find = null;
		find = database.getCollection("proba2").find();
		int num = 0;
		List<MainDocument> documents = new ArrayList<>();
		
		for (Document document : find) {						
					MainDocument doc = null;
					doc = new MainDocument(num, document.getString("Title"), document.getString("Content"),  0 , (ArrayList) document.get("entities"), (ArrayList) document.get("topics"));
                                       
			                if (doc.getTitle().contains(name)) {
				        documents.add(doc);
			                }
		}
        return documents;
    }
        
    public List<SimilarDocument> getSimilarDocuments (String title) throws IOException {
		FindIterable<Document> find = null;
		find = database.getCollection("similarDocuments").find(new Document("title", title));
		int num = 0;
		List<SimilarDocument> result = new ArrayList<>();
				
		for (Document document : find) {
				SimilarDocument simDoc = new SimilarDocument(num, document.getString("title"), document.getString("simTitle"), document.getDouble("similarity"), document.getString("simContent"));
				result.add(simDoc);
				num++;		
		}
	return result;
    }      
}
