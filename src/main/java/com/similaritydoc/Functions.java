/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.similaritydoc.textrazor.TextRazorObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.bson.Document;
/**
 *
 * @author DM
 */
public class Functions {
    
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
    
    public static void calculateSimilarity (MongoDB mongo, List<MainDocument> documents, RealMatrix tfIdfMatrix, RealMatrix topicMatrix) throws IOException {
    
        for (int i = 0; i < tfIdfMatrix.getColumnDimension(); i++) {
        //lista za slicne dokumente odredjenog dokumenta
        List<SimilarDocument> similarDocuments = new ArrayList<>();
           
            double sim = 0.0;
            for (int j = 0; j < tfIdfMatrix.getColumnDimension(); j++) {
                SimilarDocument doc;
                sim = getFinalSimilarity(getCosineSimilarity(i, j, tfIdfMatrix), getCosineSimilarity(i, j, topicMatrix));

                doc = new SimilarDocument(documents.get(i).getId(), documents.get(i).getTitle(), documents.get(j).getTitle(), sim, documents.get(j).getContent());
                similarDocuments.add(doc);
            }

            //samo naslicnijih 10 dokumenata dolaze u obzir
            Collections.sort(similarDocuments);

            for (int k = 2; k < 12; k++) {
            	mongo.getDatabase().getCollection(Functions.getProperty("COLLECTION2")).insertOne(new Document("documentTitle", similarDocuments.get(k).getTitle())
            	.append("simDocumentTitle", similarDocuments.get(k).getSimTitle())
            	.append("similarity", similarDocuments.get(k).getSimilarity())
                .append("simDocumentContent", similarDocuments.get(k).getSimContent())
                .append("id", similarDocuments.get(k).getId()));
            }

    //setovan parametar "analyzed" da bi se  znalo za koje dokumente je proracunata slicnost
    mongo.getDatabase().getCollection(Functions.getProperty("COLLECTION1")).updateOne(new Document("_id", documents.get(i).getId()),new Document("$set", new Document("analyzed", 1)));
    
        }   
    }
 
    //create dictionary of unique entities, topics..
    public static TextRazorObject createMainDictionary(List<MainDocument> documents) {
        
                List<String> entityDictionary = new ArrayList<String>();
                List<String> topicDictionary = new ArrayList<String>();
                List<List<String>> dictionaries = new ArrayList<List<String>>();
                TextRazorObject tro = null;
                
                //create main entityDictionary, topicDictionary of unique terms using list of entities, topics of each document
		for (MainDocument doc : documents) {
                    
                    if (doc.getEntities() != null && doc.getTopics() != null){
                        for (String entity : doc.getEntities()) {
			
			    if (!entityDictionary.contains(entity)) {
			    	entityDictionary.add(entity);
			    }
                      }
                      for (String topic : doc.getTopics()) {
			
			    if (!topicDictionary.contains(topic)) {
			    	topicDictionary.add(topic);
			    }
                      }
                      dictionaries.add(doc.getEntities());
                    }
                  
		}    
                
                tro = new TextRazorObject (entityDictionary, topicDictionary, dictionaries);
                
    return tro;
    }
    
    
    public static RealMatrix createBasicMatrix(TextRazorObject tro) {
        
		RealMatrix basicMatrix;
                //create matrix with where number of columns is number if documents and number of rows is number of unique entities in dictionary
		basicMatrix = MatrixUtils.createRealMatrix(tro.getEntities().size(), tro.getDictionaries().size());
                //Term Frequency in the document
		double tf = 0;
                int numberOfColumn = 0;
                
                //indicate how many times each term appears in each (dictionary of the document)
                for (List<String> dic : tro.getDictionaries()) {
                    
                    for (int i = 0; i<tro.getEntities().size(); i++) {
                        
                        for (String entity : dic) {
			  if (tro.getEntities().get(i).equalsIgnoreCase(entity)) {
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
    
   
    public static RealMatrix getTfIdfMatrix (TextRazorObject tro) throws NetworkException, AnalysisException {
 
        RealMatrix basicMatrix;
        basicMatrix = Functions.createBasicMatrix (tro);
        RealMatrix tfidfMatrix;
        tfidfMatrix = Functions.getTfIdf(basicMatrix);
        
    return normalize(tfidfMatrix);
    }
    
    public static double getFinalSimilarity(double sim1, double sim2) {
        
         double sim = 0.0;
          sim = (sim1 + sim2)/2;
    
    return sim;
    }
    
}
