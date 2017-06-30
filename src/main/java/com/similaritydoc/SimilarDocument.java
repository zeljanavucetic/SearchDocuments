/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

/**
 *
 * @author DM
 */
public class SimilarDocument {
    
        private int num;
	private String title;
	private String simTitle;
        private double similarity;
	private String simContent;
        
        public SimilarDocument (int num, String title, String simTitle, double similarity, String simContent){
        this.num=num;
        this.title=title;
        this.simTitle=simTitle;
        this.similarity=similarity;
        this.simContent=simContent;
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
     * @return the simTitle
     */
    public String getSimTitle() {
        return simTitle;
    }

    /**
     * @param simTitle the simTitle to set
     */
    public void setSimTitle(String simTitle) {
        this.simTitle = simTitle;
    }

    /**
     * @return the simContent
     */
    public String getSimContent() {
        return simContent;
    }

    /**
     * @param simContent the simContent to set
     */
    public void setSimContent(String simContent) {
        this.simContent = simContent;
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
    
}
