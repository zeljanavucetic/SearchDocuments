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
import java.util.Collections;



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
      MongoDB mongo = new MongoDB("192.168.0.17" , 27017);
      //mongo.updateDocuments();
      List<MainDocument> documentsMongo = mongo.getDocuments();
       
         tro = Functions.createMainDictionary(documentsMongo);
         topicMatrix = TopicModelMallet.getTopicDistribution(documentsMongo, tro.topics.size());
         tfIdfMatrix = getTfIdfMatrix(tro);
        
        // delete existing documentSimilarity collection
    	MongoCollection<Document> dbcoll = mongo.database.getCollection("documentSimilarity");
    	dbcoll.drop();
        mongo.database.createCollection("documentSimilarity");

    	// calculate similarity
    	System.out.println("Calculating cosine similarity...");
        for (int i = 0; i < documentsMongo.size(); i++) {
            MainDocument document = documentsMongo.get(i);
            List<MainDocument> temporaryList = new ArrayList<>();
            temporaryList.add(documentsMongo.get(i));
            double sim = 0.0;
            for (int j = 0; j < documentsMongo.size(); j++) {
                
                sim = getFinalSimilarity(getCosineSimilarity(i, j, tfIdfMatrix), getCosineSimilarity(i, j, topicMatrix));
                documentsMongo.get(j).setSimilarity(sim);
                temporaryList.add(documentsMongo.get(j));
            }

            Collections.sort(temporaryList);

            for (int k = 2; k < 12; k++) {
            	mongo.database.getCollection("projectSimilarity").insertOne(new Document("documentTitle", document.getTitle())
            	.append("simDocumentTitle", temporaryList.get(k).getTitle())
            	.append("similarity", temporaryList.get(k).getSimilarity()));
            }            
        }
        
   } 
}
