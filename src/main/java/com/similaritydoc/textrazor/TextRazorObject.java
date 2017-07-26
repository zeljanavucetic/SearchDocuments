/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.similaritydoc.textrazor;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author DM
 */
public class TextRazorObject {

     private List<String> entities;
     private List<String> topics;
     //list of entities of all documents
     private List<List<String>> dictionaries;
    
     public TextRazorObject (List<String> entities, List<String> topics, List<List<String>> dictionaries){
     this.entities = new ArrayList<String>();
     this.entities = entities;
     this.topics = new ArrayList<String>();
     this.topics=topics;
     this.dictionaries = new ArrayList<List<String>>();
     this.dictionaries = dictionaries;
     }

    /**
     * @return the entities
     */
    public List<String> getEntities() {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities(List<String> entities) {
        this.entities = entities;
    }

    /**
     * @return the topics
     */
    public List<String> getTopics() {
        return topics;
    }

    /**
     * @param topics the topics to set
     */
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    /**
     * @return the dictionaries
     */
    public List<List<String>> getDictionaries() {
        return dictionaries;
    }

    /**
     * @param dictionaries the dictionaries to set
     */
    public void setDictionaries(List<List<String>> dictionaries) {
        this.dictionaries = dictionaries;
    }
}
