/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import static com.similaritydoc.textrazor.TextRazor.analyseText;
import com.similaritydoc.textrazor.AnalysisObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

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
    
    
    public void insertDocuments(String file, String collection) {
       
             String zana = Functions.getTextFromFile(file);
             FileInputStream fstream = null;
             
    try {
        fstream = new FileInputStream(file);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        System.out.println("file doesn't exist");
        return;
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

    // read it line by line
    String strLine;
        //MongoCollection<Document> newColl =   mongo.getDatabase().getCollection("hh");
    try {
        while ((strLine = br.readLine()) != null) {
            // convert line by line to BSON
            Document doc = Document.parse(strLine);
            // insert BSONs to database
            try {
                getDatabase().getCollection(collection).insertOne(doc);
            }
            catch (MongoException e) {
              // duplicate key
              e.printStackTrace();
            }


        }
        br.close();
    } catch (IOException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
      
    }
    
    
//    public List<MainDocument> getNewDocuments() throws IOException{
//       
//        BasicDBObject query = new BasicDBObject("analyzed", new BasicDBObject("$exists", false)); 
//        FindIterable<Document> find = getDatabase().getCollection(Functions.getProperty("COLLECTION1")).find(query);
//        
//        List<MainDocument> documents = new ArrayList<>();
// 
//            for (Document document : find) {
//            
//                        MainDocument doc = null;
//		        doc = new MainDocument(document.getObjectId("_id"), document.getString("Title"), document.getString("Content"), (ArrayList) document.get("entities"), (ArrayList) document.get("topics"), (ArrayList) document.get("keyphrases"));
//			documents.add(doc);      
//	    }
//        return documents;
//    }
    
    
    public void updateDocuments(String collection) throws NetworkException, AnalysisException, IOException{
        
              String API_KEY = "";
              AnalysisObject tro = null;

              API_KEY =  Functions.getProperty("API_KEY"); 
        
        //ukoliko postoji u bazi text koji nije analiziran
        BasicDBObject query = new BasicDBObject("entities", new BasicDBObject("$exists", false)); 
        FindIterable<Document> find = getDatabase().getCollection(collection).find(query);
        
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
          
		getDatabase().getCollection(collection).updateOne(new Document("Title", document.getString("Title")),new Document("$set", new Document("entities", entities)));
                getDatabase().getCollection(collection).updateOne(new Document("Title", document.getString("Title")),new Document("$set", new Document("topics", topics)));

	} //ukoliko postoji u bazi text koji nije analiziran	
        
      }
    }
    
    
       public void findKeyphrases(String collection) throws IOException{
        
        //ukoliko postoji u bazi text koji nije analiziran
        BasicDBObject query = new BasicDBObject("keyphrases", new BasicDBObject("$exists", false)); 
        FindIterable<Document> find = getDatabase().getCollection(collection).find(query);
        
        List<MainDocument> documents = new ArrayList<>();
        List<String> phrases = null;
        if(find != null){
         for (Document document : find) {
           
           phrases = Functions.getKeyphrases(document.getString("Content"));
            BasicDBList keyphrases = new BasicDBList();
           if(phrases == null){
              keyphrases = null;
           }
           else {
                 for (int i =0; i<phrases.size(); i++){
                 keyphrases.add(phrases.get(i));
           }
                 
           }
           getDatabase().getCollection(collection).updateOne(new Document("Title", document.getString("Title")),new Document("$set", new Document("keyphrases", keyphrases)));
           phrases = null;
	} 
      }
    }
    
    
    public List<MainDocument> getDocuments(String collection) throws IOException{
       
        FindIterable<Document> find = getDatabase().getCollection(collection).find();
        List<MainDocument> documents = new ArrayList<>();
 
            for (Document document : find) {
            
                        MainDocument doc = null;
		        doc = new MainDocument(document.getObjectId("_id"), document.getString("Title"), document.getString("Content"), (ArrayList) document.get("entities"), (ArrayList) document.get("topics"), (ArrayList) document.get("keyphrases"));
			documents.add(doc);       
	    }
        return documents;
    }
   
    public List<MainDocument> getSearchResult(String name, String collection) throws IOException {
		
                FindIterable<Document> find = null;
		find = getDatabase().getCollection(collection).find();
		List<MainDocument> documents = new ArrayList<>();
		
		for (Document document : find) {						
					MainDocument doc = null;
					doc = new MainDocument(document.getObjectId("_id"), document.getString("Title"), document.getString("Content"), (ArrayList) document.get("entities"), (ArrayList) document.get("topics"), null);
                                       
			                if (doc.getTitle().contains(name)) {
				        documents.add(doc);
			                }
		}
        return documents;
    }
        
    public List<SimilarDocument> getSimilarDocuments (String title, String collection2) throws IOException {
		FindIterable<Document> find = null;
		find = getDatabase().getCollection(collection2).find(new Document("documentTitle", title));
		
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
