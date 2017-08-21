/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inf_sistem.controller;

import com.similaritydoc.Functions;
import com.similaritydoc.MongoDB;
import com.similaritydoc.MainDocument;
import com.similaritydoc.SimilarDocument;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author DM
 */
@Controller
public class SearchController {


    @RequestMapping("/")
    public ModelAndView search() throws IOException {
      
        return new ModelAndView("search.jsp");
    } 
    
    @RequestMapping("/GetDocuments")
	public @ResponseBody
	List<MainDocument> getDocuments(@RequestParam("name") String name) throws IOException {

		MongoDB db = new MongoDB();
		List<MainDocument> result = db.getSearchResult(name, Functions.getProperty("PolitikaCollection"));
                
            return result;

	}
        
    @RequestMapping("/GetSimilarDocuments")
	public @ResponseBody
	ModelAndView GetSimilarDocuments(@RequestParam("title") String title) throws IOException {
		MongoDB db = new MongoDB();
		Map<String, Object> map = new HashMap<>();
		List<SimilarDocument> similarDocuments = db.getSimilarDocuments(title, Functions.getProperty("PolitikaCollSim"));
		
		map.put("title", title);
		//map.put("content", content);
                if (similarDocuments.size() == 0) {
                }
                else{
		map.put("similarDocuments", similarDocuments);
                }
	    return new ModelAndView("similar.jsp", map);
       }
   
}

