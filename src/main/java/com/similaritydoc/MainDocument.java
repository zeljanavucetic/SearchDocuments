/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import java.util.ArrayList;

/**
 *
 * @author DM
 */
public class MainDocument implements Comparable<MainDocument>{
    
        private int num;
	private String title;
	private String content;
	private double similarity;
        public ArrayList<String> entities = new ArrayList<String>();
        public ArrayList<String> topics  = new ArrayList<String>();
        
        public MainDocument (int num, String title, String content, double similarity, ArrayList<String> entities, ArrayList<String> topics){
        this.num = num;
        this.title = title;
        this.content = content;
        this.similarity = similarity;
        this.entities = entities;
        this.topics = topics;
        }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the similarity
     */
    public double getSimilarity() {
        return similarity;
    }

    /**
     * @param similarity the similarity to set
     */
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
    
    
    @Override
    public int compareTo(MainDocument doc) {
		if (this.similarity > doc.similarity) {
            return -1;
        } else if (this.similarity == doc.similarity) {
            return 0;
        } else {
            return 1;
        }
	}
        
        

}
