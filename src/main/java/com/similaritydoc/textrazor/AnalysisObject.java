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
public class AnalysisObject {

     private List<String> entities;
     private List<String> topics;
     private List<String> keyphrases;
     //list of entities of all documents
     private List<List<String>> entityDictionaries;
     private List<List<String>> keyphrasesDictionaries;
    
     public AnalysisObject (List<String> entities, List<String> topics, List<String> keyphrases, List<List<String>> entityDictionaries, List<List<String>> keyphrasesDictionaries){
     this.entities = new ArrayList<String>();
     this.entities = entities;
     this.topics = new ArrayList<String>();
     this.topics = topics;
     this.keyphrases = new ArrayList<String>();
     this.keyphrases = keyphrases;
     this.entityDictionaries = new ArrayList<List<String>>();
     this.entityDictionaries = entityDictionaries;
     this.keyphrasesDictionaries = new ArrayList<List<String>>();
     this.keyphrasesDictionaries = keyphrasesDictionaries;
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
     * @return the entityDictionaries
     */
    public List<List<String>> getEntityDictionaries() {
        return entityDictionaries;
    }

    /**
     * @param entityDictionaries the entityDictionaries to set
     */
    public void setEntityDictionaries(List<List<String>> entityDictionaries) {
        this.entityDictionaries = entityDictionaries;
    }

    /**
     * @return the keyphrases
     */
    public List<String> getKeyphrases() {
        return keyphrases;
    }

    /**
     * @param keyphrases the keyphrases to set
     */
    public void setKeyphrases(List<String> keyphrases) {
        this.keyphrases = keyphrases;
    }

    /**
     * @return the keyphrasesDictionaries
     */
    public List<List<String>> getKeyphrasesDictionaries() {
        return keyphrasesDictionaries;
    }

    /**
     * @param keyphrasesDictionaries the keyphrasesDictionaries to set
     */
    public void setKeyphrasesDictionaries(List<List<String>> keyphrasesDictionaries) {
        this.keyphrasesDictionaries = keyphrasesDictionaries;
    }
}
