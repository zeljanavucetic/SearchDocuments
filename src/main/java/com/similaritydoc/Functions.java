/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.similaritydoc.textrazor.AnalysisObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.bson.Document;
import org.json.JSONObject;
/**
 *
 * @author DM
 */
public class Functions {
    
    
    public static String getTextFromFile(String fileName) {

        // This will reference one line at a time
        String line = null;
        String text = "";

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                text = text + " " + line ;
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        return text;
    }
    
    public static String readUrl(String urlString) throws IOException {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    }
	    
	    catch (IOException e) {
	    	return null;
	    }
	    
	    finally {
	        if (reader != null)
	            reader.close();
	    }
    }
    
    //get keyphrases from text using KeyphrasesExtraction API
    public static List<String> getKeyphrases(String text) throws FileNotFoundException, IOException{
     
        String address = Functions.getProperty("PhraseSERVICE");
        
        List<String> keyphrases = new ArrayList<String>();
        
        String query = String.format("text=%s", URLEncoder.encode(text, "UTF-8"));
        
                String result = "";
		result = readUrl(address + "?" + query);
                
                if(result == null){
                keyphrases = null;
                }
                else{
                JSONObject json = new JSONObject(result);
                
                Iterator<?> keys = json.keys();
			
			while(keys.hasNext()) {
			    String key = (String)keys.next();
			    	keyphrases.add(key);

			}
                }
    return keyphrases;
    }
    
    
    //get properties from config file
    public static String getProperty(String name) throws FileNotFoundException, IOException{
        
              String property = "";
              Properties prop = new Properties();
	      InputStream input = null;

        
        	try {
                
                    input = new FileInputStream("src/main/resources/config.properties");

		// load a properties file
		prop.load(input);
                property =  prop.getProperty(name); 
                
                } catch (IOException ex) {
		 ex.printStackTrace();
	        }
                
    return property;
    }
    
    public static void calculateSimilarity (MongoDB mongo, List<MainDocument> documents, RealMatrix entityMatrix, RealMatrix topicMatrix, RealMatrix keyphrasesMatrix, String collection2) throws IOException {
       
        for (int i = 0; i < entityMatrix.getColumnDimension(); i++) {
        //lista za slicne dokumente odredjenog dokumenta
        List<SimilarDocument> similarDocuments = new ArrayList<>();
           
            double sim = 0.0;
            for (int j = 0; j < entityMatrix.getColumnDimension(); j++) {
                SimilarDocument doc;
                sim = getFinalSimilarity(getCosineSimilarity(i, j, entityMatrix), getCosineSimilarity(i, j, topicMatrix), getCosineSimilarity(i, j, keyphrasesMatrix));

                doc = new SimilarDocument(documents.get(i).getId(), documents.get(i).getTitle(), documents.get(j).getTitle(), sim, documents.get(j).getContent());
                similarDocuments.add(doc);
            }

            //samo naslicnijih 10 dokumenata dolaze u obzir
            Collections.sort(similarDocuments);

            for (int k = 2; k < 12; k++) {
            	mongo.getDatabase().getCollection(collection2).insertOne(new Document("documentTitle", similarDocuments.get(k).getTitle())
            	.append("simDocumentTitle", similarDocuments.get(k).getSimTitle())
            	.append("similarity", similarDocuments.get(k).getSimilarity())
                .append("simDocumentContent", similarDocuments.get(k).getSimContent())
                .append("id", similarDocuments.get(k).getId()));
            }
    
        }   
    }
 
    //create dictionary of unique entities, topics..
    public static AnalysisObject createMainDictionary(List<MainDocument> documents) {
        
                List<String> entityDictionary = new ArrayList<String>();
                List<String> topicDictionary = new ArrayList<String>();
                List<String> keyphrasesDictionary = new ArrayList<String>();
                List<List<String>> entityDictionaries = new ArrayList<List<String>>();
                List<List<String>> keyphrasesDictionaries = new ArrayList<List<String>>();
                AnalysisObject tro = null;
                
                //create main entityDictionary, topicDictionary of unique terms using list of entities, topics of each document
		for (MainDocument doc : documents) {
                    
                    //if (doc.getEntities() != null && doc.getTopics() != null && doc.getKeyphrases()!= null){}
                    
                    if (doc.getEntities() != null){
                        for (String entity : doc.getEntities()) {
			
			    if (!entityDictionary.contains(entity)) {
			    	entityDictionary.add(entity);
			    }
                      }
                    }
                    
                    if (doc.getTopics() != null){
                      for (String topic : doc.getTopics()) {
			
			    if (!topicDictionary.contains(topic)) {
			    	topicDictionary.add(topic);
			    }
                      }
                    }
                      
                     if (doc.getKeyphrases()!= null){
                         for (String keyphrase : doc.getKeyphrases()) {
			
			    if (!keyphrasesDictionary.contains(keyphrase)) {
			    	keyphrasesDictionary.add(keyphrase);
			    }
                      }
                     }
                      
                      entityDictionaries.add(doc.getEntities());
                      keyphrasesDictionaries.add(doc.getKeyphrases());
                    
                  
		}    
                
                tro = new AnalysisObject (entityDictionary, topicDictionary, keyphrasesDictionary, entityDictionaries, keyphrasesDictionaries);
                
    return tro;
    }
    
    
     //create dictionary of unique keyphrases..
    public static List<String> createKeyphrasesDictionary(List<MainDocument> documents) {
        
                List<String> keyphrasesDictionary = new ArrayList<String>();
                
                //da li postoji podudaranje tj. da li postoje identicne fraze za razlicite tekstove - DA
                List<String> provera = new ArrayList<String>();
                
                //create main entityDictionary, topicDictionary of unique terms using list of entities, topics of each document
		for (MainDocument doc : documents) {
                    
                    if (doc.getKeyphrases()!= null){
                        for (String keyphrase : doc.getKeyphrases()) {
			
			    if (!keyphrasesDictionary.contains(keyphrase)) {
			    	keyphrasesDictionary.add(keyphrase);
			    }
                            provera.add(keyphrase);
                      }
                    }
                  
		}    
                
    return keyphrasesDictionary;
    }
    
    
    public static RealMatrix createBasicMatrix(List<String> dictionary, List<List<String>> dictionaries) {
        
		RealMatrix basicMatrix;
                //create matrix with where number of columns is number if documents and number of rows is number of unique entities in dictionary
		basicMatrix = MatrixUtils.createRealMatrix(dictionary.size(), dictionaries.size());
                //Term Frequency in the document
		double tf = 0;
                int numberOfColumn = 0;
                
                //indicate how many times each term appears in each (dictionary of the document)
                for (List<String> dic : dictionaries) {
                    
                    for (int i = 0; i<dictionary.size(); i++) {
                        
                        for (String entity : dic) {
			  if (dictionary.get(i).equalsIgnoreCase(entity)) {
                              tf++;
                          }	
                        }
                        basicMatrix.setEntry(i, numberOfColumn, tf/dic.size());
                        tf=0;
                    }
                    
                 numberOfColumn++;
                }
    return basicMatrix;
    }
    
 
    public static RealMatrix getTfIdf(RealMatrix matrix) {
        //Term Frequency
        double tf;
        //Inverse Document Frequency
        double idf;
        double ndf;
        int numDocumentsWithTheTerm = 0;
        
        //for each term in the dictionary (row of the matrix) calculate tfidf
        for (int i = 0; i < matrix.getRowDimension(); i++) {
      
        //get number of documents with the term in it (get number of columns of the matrix)
        for (int j = 0; j < matrix.getColumnDimension(); j++) {
            if (matrix.getEntry(i, j) != 0) {
                numDocumentsWithTheTerm++;
            }
        }
       //calculate tf-idf
        for (int j = 0; j < matrix.getColumnDimension(); j++) {
                //tf = Math.sqrt(matrix.getEntry(i, j));
                tf = matrix.getEntry(i, j);
                ndf = matrix.getColumnDimension()/numDocumentsWithTheTerm+1;
                idf = Math.log(ndf+1);
                matrix.setEntry(i, j, idf * tf);
            }
        numDocumentsWithTheTerm = 0;
        }
        
    return matrix;
    }
        
    // normalization of the matrix
    public static RealMatrix normalize(RealMatrix realMatrix) {
        //Sum of values for the column
        double sumValCol = 0;

        //create matrix of one row to put sum of values (sumValCol) for every column in realMatrix
        RealMatrix row = MatrixUtils.createRealMatrix(1, realMatrix.getColumnDimension());
        
        for (int i = 0; i < realMatrix.getColumnDimension(); i++) {
            sumValCol = 0;
            for (int j = 0; j < realMatrix.getRowDimension(); j++) {
                sumValCol += Math.pow(realMatrix.getEntry(j, i), 2);
            }
            sumValCol = Math.sqrt(sumValCol);
            row.setEntry(0, i, sumValCol);
        }
        for (int i = 0; i < realMatrix.getColumnDimension(); i++) {
            for (int j = 0; j < realMatrix.getRowDimension(); j++) {
            	realMatrix.setEntry(j, i, realMatrix.getEntry(j, i) / row.getEntry(0, i));
            }
        }
    return realMatrix;
    }   
    
    //get documents similarity using cosine measure
    public static double getCosineSimilarity(int doc1, int doc2, RealMatrix matrix) {
        
        double similarity = 0;
        double sum = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (int j = 0; j < matrix.getRowDimension(); j++) {
            sum += matrix.getEntry(j, doc1) * matrix.getEntry(j, doc2);
            magnitude1 += Math.pow(matrix.getEntry(j, doc1), 2);
            magnitude2 += Math.pow(matrix.getEntry(j, doc1), 2);
        }
        similarity = sum / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    return similarity;
    }
    
   
    public static RealMatrix getTfIdfMatrix (List<String> dictionary, List<List<String>> dictionaries) throws NetworkException, AnalysisException {
 
        RealMatrix basicMatrix;
        basicMatrix = Functions.createBasicMatrix (dictionary, dictionaries);
        RealMatrix tfidfMatrix;
        tfidfMatrix = Functions.getTfIdf(basicMatrix);
        
    return normalize(tfidfMatrix);
    }
    
    public static double getFinalSimilarity(double sim1, double sim2, double sim3) {
        
         double sim = 0.0;
          sim = (sim1 + sim2 + sim3)/3;
    
    return sim;
    }
    
}
