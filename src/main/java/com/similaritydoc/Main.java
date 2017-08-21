/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import static com.similaritydoc.Functions.*;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.io.*;


/**
 *
 * @author DM
 */
public class Main {
      
    public static void main(String[] args) throws IOException, NetworkException, NetworkException, AnalysisException {
            
      //connect to mongo database
      MongoDB mongo = new MongoDB();
      
    run(mongo, Functions.getProperty("PolitikaFile"), Functions.getProperty("PolitikaCollection"), Functions.getProperty("PolitikaCollSim"));
    run(mongo, Functions.getProperty("ScienceFile"), Functions.getProperty("ScienceCollection"), Functions.getProperty("ScienceCollSim"));
    run(mongo, Functions.getProperty("SportFile"), Functions.getProperty("SportCollection"), Functions.getProperty("SportCollSim"));
        
   } 
    
    
 
}
