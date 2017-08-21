/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.mongodb.client.MongoCollection;
import static com.similaritydoc.Functions.*;
import com.similaritydoc.textrazor.AnalysisObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.util.*;
import java.io.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.bson.Document;


/**
 *
 * @author DM
 */
public class Main {
      
    public static void main(String[] args) throws IOException, NetworkException, NetworkException, AnalysisException {

         AnalysisObject tro = null;
         RealMatrix entityMatrix;
         RealMatrix topicMatrix;
         RealMatrix keyphrasesMatrix;
            
      //connect to mongo database
      MongoDB mongo = new MongoDB();

      //insert documents into certain collection in MongoDB
       mongo.insertDocuments(Functions.getProperty("PolitikaFile"), Functions.getProperty("PolitikaCollection"));
   
      //ukoliko postoji u bazi text koji nije analiziran pomocu Text Razora
       mongo.updateDocuments(Functions.getProperty("PolitikaCollection"));
      
      //ukoliko postoji u bazi text iz koga nizu izdvojene fraze
       mongo.findKeyphrases(Functions.getProperty("PolitikaCollection"));
      
      List<MainDocument> documentsMongo = mongo.getDocuments(Functions.getProperty("PolitikaCollection"));
      
         tro = Functions.createMainDictionary(documentsMongo);
         
         topicMatrix = TopicModelMallet.getTopicDistribution(documentsMongo, tro.getTopics().size());
         entityMatrix = getTfIdfMatrix(tro.getEntities(), tro.getEntityDictionaries());
         keyphrasesMatrix = getTfIdfMatrix(tro.getKeyphrases(), tro.getKeyphrasesDictionaries());
        
        // delete existing documentSimilarity collection
    	MongoCollection<Document> dbcoll = mongo.getDatabase().getCollection(Functions.getProperty("PolitikaCollSim"));
   	dbcoll.drop();
        mongo.getDatabase().createCollection(Functions.getProperty("PolitikaCollSim"));
      
        // calculate similarity for NEW documents in database
        Functions.calculateSimilarity(mongo, documentsMongo, entityMatrix, topicMatrix, keyphrasesMatrix, Functions.getProperty("PolitikaCollSim"));
        
   } 
}
