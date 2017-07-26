/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import static com.similaritydoc.textrazor.TextRazor.analyseText;
import com.similaritydoc.textrazor.TextRazorObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author DM
 */
public class MongoDB {
    
    private MongoClient mongoClient;    
    private com.mongodb.client.MongoDatabase database; 
    private String address;
    
    public MongoDB () throws IOException {
    	this.mongoClient = new MongoClient(Functions.getProperty("IP_ADDRESS"), 27017);
    	this.database = mongoClient.getDatabase(Functions.getProperty("DATABASE"));
    }
    
    
    public List<MainDocument> getNewDocuments() throws IOException{
       
        BasicDBObject query = new BasicDBObject("analyzed", new BasicDBObject("$exists", false)); 
        FindIterable<Document> find = getDatabase().getCollection(Functions.getProperty("COLLECTION1")).find(query).limit(20);
        
        List<MainDocument> documents = new ArrayList<>();
 
            for (Document document : find) {
            
                        MainDocument doc = null;
		        doc = new MainDocument(document.getObjectId("_id"), document.getString("Title"), document.getString("Content"), (ArrayList) document.get("entities"), (ArrayList) document.get("topics"));
			documents.add(doc);      
	    }
        return documents;
    }
    
    
    public void updateDocuments() throws NetworkException, AnalysisException, IOException{
        
              String API_KEY = "";
              TextRazorObject tro = null;

              API_KEY =  Functions.getProperty("API_KEY"); 
        
        //ukoliko postoji u bazi text koji nije analiziran
        BasicDBObject query = new BasicDBObject("entities", new BasicDBObject("$exists", false)); 
        FindIterable<Document> find = getDatabase().getCollection(Functions.getProperty("COLLECTION1")).find(query);
        
        if(find != null){
         for (Document document : find) {
            
           tro = analyseText(API_KEY, document.getString("Content"));
           BasicDBList entities = new BasicDBList();
           BasicDBList topics = new BasicDBList();
           
           for (int i =0; i<tro.getEntities().size(); i++){
           entities.add(tro.getEntities().get(i));
           }
           for (int j=0; j<tro.getTopics().size(); j++){
           topics.add(tro.getTopics().get(j));
           }
          
		getDatabase().getCollection(Functions.getProperty("COLLECTION1")).updateOne(new Document("Title", document.getString("Title")),new Document("$set", new Document("entities", entities)));
                getDatabase().getCollection(Functions.getProperty("COLLECTION1")).updateOne(new Document("Title", document.getString("Title")),new Document("$set", new Document("topics", topics)));

	} //ukoliko postoji u bazi text koji nije analiziran	
        
      }
    }
    public List<MainDocument> getDocuments() throws IOException{
       
        FindIterable<Document> find = getDatabase().getCollection(Functions.getProperty("COLLECTION1")).find().limit(20);
        List<MainDocument> documents = new ArrayList<>();
 
            for (Document document : find) {
            
                        MainDocument doc = null;
		        doc = new MainDocument(document.getObjectId("_id"), document.getString("Title"), document.getString("Content"), (ArrayList) document.get("entities"), (ArrayList) document.get("topics"));
			documents.add(doc);       
	    }
        return documents;
    }
   
    public List<MainDocument> getSearchResult(String name) throws IOException {
		
                FindIterable<Document> find = null;
		find = getDatabase().getCollection(Functions.getProperty("COLLECTION1")).find();
		List<MainDocument> documents = new ArrayList<>();
		
		for (Document document : find) {						
					MainDocument doc = null;
					doc = new MainDocument(document.getObjectId("_id"), document.getString("Title"), document.getString("Content"), (ArrayList) document.get("entities"), (ArrayList) document.get("topics"));
                                       
			                if (doc.getTitle().contains(name)) {
				        documents.add(doc);
			                }
		}
        return documents;
    }
        
    public List<SimilarDocument> getSimilarDocuments (String title) throws IOException {
		FindIterable<Document> find = null;
		find = getDatabase().getCollection(Functions.getProperty("COLLECTION2")).find(new Document("documentTitle", title));
		
		List<SimilarDocument> result = new ArrayList<>();
				
		for (Document document : find) {
				SimilarDocument simDoc = new SimilarDocument(document.getObjectId("id") , document.getString("documentTitle"), document.getString("simDocumentTitle"), document.getDouble("similarity"), document.getString("simDocumentContent"));
				result.add(simDoc);		
		}
	return result;
    }      

    /**
     * @return the mongoClient
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    /**
     * @param mongoClient the mongoClient to set
     */
    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * @return the database
     */
    public com.mongodb.client.MongoDatabase getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(com.mongodb.client.MongoDatabase database) {
        this.database = database;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
