package org.amalic.servicefromdata;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenericD2SController {
    static final String PATTERN = "/d2s/**";
    
    @Autowired
	private RdfRepository repo;

    @RequestMapping("/d2s/**")
    public Object genericHandler(HttpServletRequest request) {
//    	System.out.println(" - " + request.getQueryString());   	
    	String[] params = request.getRequestURI().substring(PATTERN.length()-2).split("/");
    	String dataset = params[0];
    	String clazz = params[1];
    	
    	String sparql="select * where { {select ?drug where {" + 
    			"graph <http://data2services/biolink/" + dataset + "> {" + 
    			"?drug a <http://bioentity.io/vocab/" + clazz + ">" + 
    			"} } LIMIT 100 } " + 
    			"?drug ?p ?o ." + 
    			"filter(!isblank(?o)) }";
    	
    	return repo.executeSparql(sparql);
    }
    
}
