package org.amalic.servicefromdata.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/biolink")
public class ServiceFromData {
//	private static final Logger logger = Logger.getLogger(ServiceFromData.class.getName());
    static final Long LIMIT = 1000L;
    
    @Autowired
	private RdfRepository repo;
    
    @RequestMapping(value = "/datasets", method = RequestMethod.GET)
    public void datasets(
    		HttpServletRequest request
    		, HttpServletResponse response
    		) throws IOException {
    	String sparql = 
    			"PREFIX void: <http://rdfs.org/ns/void#>\n"
    			+ "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n"
    			+ "PREFIX dct: <http://purl.org/dc/terms/>\n"
    			+ "PREFIX idot: <http://identifiers.org/idot/>\n"
    			+ "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n"
    			+ "SELECT ?dataset\n" + 
	    			"WHERE {\n" + 
	    			"        ?ds a dctypes:Dataset ;\n" + 
	    			"            idot:preferredPrefix ?dataset .\n" + 
	    			"        ?version dct:isVersionOf ?ds ; \n" + 
	    			"            dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
	    			"}";
    	
    	repo.executeSparql(sparql, response.getOutputStream(), ResultAs.CSV);
    }
    
    @RequestMapping(value = "/{dataset}", method = RequestMethod.GET)
    public void classes(
    		HttpServletRequest request
    		, HttpServletResponse response
    		, @PathVariable String dataset
    		) throws IOException {
    	String sparql = String.format(
    			"PREFIX bl: <http://w3id.org/biolink/vocab/>\n" + 
    			"PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n" + 
    			"PREFIX void: <http://rdfs.org/ns/void#>\n" + 
    			"PREFIX dcat: <http://www.w3.org/ns/dcat#>\n" + 
    			"PREFIX idot: <http://identifiers.org/idot/>\n" + 
    			"PREFIX dct: <http://purl.org/dc/terms/>PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
    			"SELECT ?source ?class ?count\n" + 
    			"WHERE\n" + 
    			"{\n" + 
    			"    {\n" + 
    			"        SELECT ?source ?classUri (count(?classUri) as ?count)  \n" + 
    			"        WHERE {\n" + 
    			"            ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .\n" + 
    			"            ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
    			"            FILTER(?source = \"%s\")\n" + 
    			"            graph ?graph {\n" + 
    			"                [] a ?classUri ;\n" + 
    			"                   bl:id ?id .\n" +
    			"            }\n" + 
    			"        }\n" + 
    			"        group by ?source ?classUri\n" + 
    			"        order by desc(?count)\n" + 
    			"    }\n" + 
    			"    BIND(strafter(str(?classUri),\"http://w3id.org/biolink/vocab/\") as ?class)\n" +
    			"    FILTER(strlen(?class) > 0)\n" + 
    			"}"
    			, dataset);
    	
    	repo.executeSparql(sparql, response.getOutputStream(), ResultAs.TSV);
    }
    
    @RequestMapping(value = "/{dataset}/{class}", method = RequestMethod.GET)
    public void datasetClass(
    		HttpServletRequest request
    		, HttpServletResponse response
    		, @PathVariable("dataset") String source
    		, @PathVariable("class") String className
    		, @RequestParam(required=false) Long page
    		) throws IOException {
    	String sparql=String.format(
    			"PREFIX bl: <http://w3id.org/biolink/vocab/>\n"
    			+ "PREFIX void: <http://rdfs.org/ns/void#>\n"
    			+ "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n"
    			+ "PREFIX dct: <http://purl.org/dc/terms/>\n"
    			+ "PREFIX idot: <http://identifiers.org/idot/>\n"
    			+ "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n"
    			+ "SELECT ?source ?class ?id\n" + 
	    			"WHERE \n" + 
	    			"{   \n" + 
	    			"    ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .\n" + 
	    			"    ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
	    			"    FILTER(?source = \"%s\")\n" + 
	    			"    GRAPH ?graph \n" + 
	    			"    {\n" + 
	    			"        ?entityUri a ?class .\n" + 
	    			"        ?entityUri a bl:%s .\n" + 
	    			"        ?entityUri bl:id ?id\n" + 
	    			"    }\n" + 
	    			"}"
				, source
				, className);
    	
    	if(page==null || page < 1)
    		page = 1L;
    	
    	sparql += " OFFSET " + ((page - 1L) * LIMIT)
    			+ " LIMIT " + LIMIT;
    	
    	repo.executeSparql(sparql, response.getOutputStream(), ResultAs.JSON);
    }
    
    @RequestMapping(value = "/{dataset}/{class}/{id}", method = RequestMethod.GET)
    public void sourceClassId(
    		HttpServletRequest request
    		, HttpServletResponse response
    		, @PathVariable("dataset") String source
    		, @PathVariable("class") String className
    		, @PathVariable("id") String id
    		) throws IOException {
    	String sparql=String.format(
    			"PREFIX bl: <http://w3id.org/biolink/vocab/>\n"
    					+ "PREFIX void: <http://rdfs.org/ns/void#>\n"
    					+ "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n"
    					+ "PREFIX dct: <http://purl.org/dc/terms/>\n"
    					+ "PREFIX idot: <http://identifiers.org/idot/>\n"
    					+ "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n"
    					+ "SELECT ?source ?class ?entity ?property ?value\n" + 
    					"WHERE\n" + 
    					"{\n" + 
    					"    ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .\n" + 
    					"    ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
    					"    FILTER(?source = \"%s\")\n" + 
    					"    GRAPH ?graph\n" + 
    					"    {\n" + 
    					"        ?entityUri a bl:%s .\n" + 
    					"        ?entityUri a ?class .\n" + 
    					"        ?entityUri bl:id ?entity .\n" + 
    					"        ?entityUri ?property ?value .\n" + 
    					"        FILTER(?entity = \"%s\")\n" + 
    					"    }\n" + 
    					"}"
    					, source
    					, className
    					, id);
    	
    	repo.executeSparql(sparql, response.getOutputStream(), ResultAs.XML);
    }
    
}
