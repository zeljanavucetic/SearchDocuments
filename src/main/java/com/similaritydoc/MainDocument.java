/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc;

import java.util.ArrayList;
import org.bson.types.ObjectId;

/**
 *
 * @author DM
 */
public class MainDocument{
    
        private ObjectId id;
	private String title;
	private String content;
        private ArrayList<String> entities = new ArrayList<String>();
        private ArrayList<String> topics  = new ArrayList<String>();
        private ArrayList<String> keyphrases;
        
        public MainDocument (ObjectId num, String title, String content,  ArrayList<String> entities, ArrayList<String> topics, ArrayList<String> keyphrases){
        this.id = num;
        this.title = title;
        this.content = content;
        this.entities = entities;
        this.topics = topics;
        this.keyphrases=keyphrases;
        }

    /**
     * @return the id
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(ObjectId id) {
        this.id = id;
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
     * @return the entities
     */
    public ArrayList<String> getEntities() {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities(ArrayList<String> entities) {
        this.entities = entities;
    }

    /**
     * @return the topics
     */
    public ArrayList<String> getTopics() {
        return topics;
    }

    /**
     * @param topics the topics to set
     */
    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    /**
     * @return the keyphrases
     */
    public ArrayList<String> getKeyphrases() {
        return keyphrases;
    }

    /**
     * @param keyphrases the keyphrases to set
     */
    public void setKeyphrases(ArrayList<String> keyphrases) {
        this.keyphrases = keyphrases;
    }      

}
