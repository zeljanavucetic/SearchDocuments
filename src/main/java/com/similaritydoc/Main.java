/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import static com.similaritydoc.Functions.*;
import com.similaritydoc.textrazor.AnalysisObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.util.*;
import java.io.*;
import org.apache.commons.math3.linear.RealMatrix;


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
      //ukoliko postoji u bazi text koji nije analiziran pomocu Text Razora
       mongo.updateDocuments();
      
       //ukoliko postoji u bazi text iz koga nizu izdvojene fraze
       mongo.findKeyphrases();
      
 
      
      List<MainDocument> documentsMongo = mongo.getNewDocuments();
      
         tro = Functions.createMainDictionary(documentsMongo);
         
         topicMatrix = TopicModelMallet.getTopicDistribution(documentsMongo, tro.getTopics().size());
         entityMatrix = getTfIdfMatrix(tro.getEntities(), tro.getEntityDictionaries());
         keyphrasesMatrix = getTfIdfMatrix(tro.getKeyphrases(), tro.getKeyphrasesDictionaries());
        
//        // delete existing documentSimilarity collection  --- VISAK
//    	MongoCollection<Document> dbcoll = mongo.getDatabase().getCollection("similarDocuments");
//    	dbcoll.drop();
//        mongo.getDatabase().createCollection("similarDocuments");
        
        // calculate similarity for NEW documents in database
        Functions.calculateSimilarity(mongo, documentsMongo, entityMatrix, topicMatrix, keyphrasesMatrix);
        
   } 
}
