/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.mongodb.client.MongoCollection;
import static com.similaritydoc.Functions.*;
import com.similaritydoc.textrazor.TextRazorObject;
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

         TextRazorObject tro = null;
         RealMatrix tfIdfMatrix;
         RealMatrix topicMatrix;
         
      //connect to mongo database
      MongoDB mongo = new MongoDB();
      //ukoliko postoji u bazi text koji nije analiziran
      mongo.updateDocuments();
      List<MainDocument> documentsMongo = mongo.getNewDocuments();
       
         tro = Functions.createMainDictionary(documentsMongo);
         topicMatrix = TopicModelMallet.getTopicDistribution(documentsMongo, tro.getTopics().size());
         tfIdfMatrix = getTfIdfMatrix(tro);
        
//        // delete existing documentSimilarity collection  --- VISAK
//    	MongoCollection<Document> dbcoll = mongo.getDatabase().getCollection("similarDocuments");
//    	dbcoll.drop();
//        mongo.getDatabase().createCollection("similarDocuments");
        
        // calculate similarity for NEW documents in database
        Functions.calculateSimilarity(mongo, documentsMongo, tfIdfMatrix, topicMatrix);
        
   } 
}
