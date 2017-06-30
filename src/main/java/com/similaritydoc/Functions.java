/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import com.similaritydoc.textrazor.TextRazorObject;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

/**
 *
 * @author DM
 */
public class Functions {
 
    //create dictionary of unique entities, topics..
    public static TextRazorObject createMainDictionary(List<MainDocument> documents) {
        
                List<String> entityDictionary = new ArrayList<String>();
                List<String> topicDictionary = new ArrayList<String>();
                List<List<String>> dictionaries = new ArrayList<List<String>>();
                TextRazorObject tro = null;
                
                //create main entityDictionary, topicDictionary of unique terms using list of entities, topics of each document
		for (MainDocument doc : documents) {
                    
                    if (doc.entities!=null && doc.topics!=null){
                        for (String entity : doc.entities) {
			
			    if (!entityDictionary.contains(entity)) {
			    	entityDictionary.add(entity);
			    }
                      }
                      for (String topic : doc.topics) {
			
			    if (!topicDictionary.contains(topic)) {
			    	topicDictionary.add(topic);
			    }
                      }
                      dictionaries.add(doc.entities);
                    }
                  
		}    
                
                tro = new TextRazorObject (entityDictionary, topicDictionary, dictionaries);
                
    return tro;
    }
    
    
    public static RealMatrix createBasicMatrix(TextRazorObject tro) {
        
		RealMatrix basicMatrix;
                //create matrix with where number of columns is number if documents and number of rows is number of unique entities in dictionary
		basicMatrix = MatrixUtils.createRealMatrix(tro.entities.size(), tro.dictionaries.size());
                //Term Frequency in the document
		double tf = 0;
                int numberOfColumn = 0;
                
                //indicate how many times each term appears in each (dictionary of the document)
                for (List<String> dic : tro.dictionaries) {
                    
                    for (int i = 0; i<tro.entities.size(); i++) {
                        
                        for (String entity : dic) {
			  if (tro.entities.get(i).equalsIgnoreCase(entity)) {
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
                tf = Math.sqrt(matrix.getEntry(i, j));
                ndf = matrix.getColumnDimension()/numDocumentsWithTheTerm+1;
                idf = Math.log(ndf+1);
                matrix.setEntry(i, j, idf * tf);
            }
        numDocumentsWithTheTerm = 0;
        }
        
    return matrix;
    //return matrix;
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
