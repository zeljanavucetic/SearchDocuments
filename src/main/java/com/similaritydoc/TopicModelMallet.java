/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.topics.*;
import static com.similaritydoc.Functions.normalize;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author DM
 */
public class TopicModelMallet {
    
    public static InstanceList createBasicList () {
    
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
        
        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add( new CharSequenceLowercase() );
        boolean add = pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplist/en.txt"), "UTF-8", false, false, false) );
        pipeList.add( new TokenSequence2FeatureSequence() );
        
        InstanceList instances = new InstanceList (new SerialPipes(pipeList));
        
        return instances; 
    }//initializeBaseList
    
    
    public static InstanceList initializeInstancesList (List<MainDocument> documents){
    
        InstanceList instances = createBasicList();
        
        String name = "";
        
        for(int i =0 ; i< documents.size(); i++) {   
        name = "Document " +i;
        instances.addThruPipe(new Instance(documents.get(i).getContent(), null, name, null));
        }
        return instances;
    }
    
    public static ParallelTopicModel createTopicModel (List<MainDocument> documents, int numTopics) throws IOException {
    
        InstanceList instances = initializeInstancesList(documents);
        
                 // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
                 //  Note that the first parameter is passed as the sum over topics, while
                 //  the second is the parameter for a single dimension of the Dirichlet prior.

		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and combine
		//  statistics after every iteration.
		model.setNumThreads(2);
                
                // Run the model for 50 iterations and stop (this is for testing only, 
		//  for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(50);
		model.estimate();
        
    return model;
    }//createTopicModel 
    

    public static RealMatrix getTopicDistribution (List<MainDocument> documents, int numTopics) throws IOException {
        
        ParallelTopicModel model = createTopicModel (documents, numTopics);
        
        RealMatrix topicMatrix;

	// Get an array of sorted sets of word ID/count pairs
	ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
                
                //create matrix with where number of columns is number of documents and number of rows is number of unique entities in dictionary
		topicMatrix = MatrixUtils.createRealMatrix(model.getNumTopics(), documents.size());
                
                // Show top 5 words in topics with proportions for the first document
		for (int i = 0; i < topicMatrix.getRowDimension(); i++) {
		      
                      Iterator<IDSorter> iterator = topicSortedWords.get(i).iterator();
                        
                      for (int j = 0; j < topicMatrix.getColumnDimension(); j++) {
                               
                        //System.out.println("Redni broj teme: " + i + " , Redni broj dokumenta: " + j);
                        //System.out.println("Topic distribution te teme za taj dokument je : "+ model.getTopicProbabilities(j)[i]);
                           
                                topicMatrix.setEntry(i, j, model.getTopicProbabilities(j)[i]);
                               
                           }      
		}
    return normalize(topicMatrix);
    }//getTopicDistribution
}
