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

     public List<String> entities;
     public List<String> topics;
     //list of entities of all documents
     public List<List<String>> dictionaries;
    
     public TextRazorObject (List<String> entities, List<String> topics, List<List<String>> dictionaries){
     this.entities = new ArrayList<String>();
     this.entities = entities;
     this.topics = new ArrayList<String>();
     this.topics=topics;
     this.dictionaries = new ArrayList<List<String>>();
     this.dictionaries = dictionaries;
     }
}
