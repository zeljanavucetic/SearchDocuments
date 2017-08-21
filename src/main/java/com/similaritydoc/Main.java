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
      
    run(mongo, Functions.getProperty("PolitikaFile"), Functions.getProperty("PolitikaCollection"), Functions.getProperty("PolitikaCollSim"));
    run(mongo, Functions.getProperty("ScienceFile"), Functions.getProperty("ScienceCollection"), Functions.getProperty("ScienceCollSim"));
    run(mongo, Functions.getProperty("SportFile"), Functions.getProperty("SportCollection"), Functions.getProperty("SportCollSim"));
        
   } 
    
    
 
}
