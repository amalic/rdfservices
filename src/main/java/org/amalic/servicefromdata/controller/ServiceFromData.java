package org.amalic.servicefromdata.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/biolink/v1")
public class ServiceFromData {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ServiceFromData.class.getName());
    static final Long LIMIT = 1000L;
    
    static final String PREFIX = 
    		"PREFIX bl: <http://w3id.org/biolink/vocab/>\n"
			+ "PREFIX void: <http://rdfs.org/ns/void#>\n"
			+ "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n"
			+ "PREFIX dct: <http://purl.org/dc/terms/>\n"
			+ "PREFIX idot: <http://identifiers.org/idot/>\n"
			+ "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n";
    
    @Autowired
	private RdfRepository rdfRepo;
    
    @ApiOperation(value="This api call returns all datasets, which can be used as input for other services. Note that the first line in csv is the header.")
    @RequestMapping(value = "/datasets"
    	, method = RequestMethod.GET
    	, produces = {"application/json", "application/xml", "text/csv", "text/tsv"})
    public void datasets(
    		HttpServletRequest request
    		, HttpServletResponse response
    		) throws IOException {
    	String sparql = 
    			PREFIX +
    				"SELECT ?dataset\n" + 
	    			"WHERE {\n" + 
	    			"        ?ds a dctypes:Dataset ;\n" + 
	    			"            idot:preferredPrefix ?dataset .\n" + 
	    			"        ?version dct:isVersionOf ?ds ; \n" + 
	    			"            dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
	    			"}";
    	handleRequestResponse(request, response, sparql);
    }
    
    @ApiOperation(value="This all classes for this particular data-set with instances having an id.")
    @RequestMapping(value = "/{dataset}"
	    , method = RequestMethod.GET
	    , produces = {"application/json", "application/xml", "text/csv", "text/tsv"})
    public void classes(
    		HttpServletRequest request
    		, HttpServletResponse response
    		, @PathVariable String dataset
    		) throws IOException {
    	String sparql = String.format(PREFIX + 
    			"SELECT ?dataset ?class ?count\n" + 
    			"WHERE {\n" + 
    			"    {\n" + 
    			"        SELECT ?dataset ?classUri (count(?classUri) as ?count)  \n" + 
    			"        WHERE {\n" + 
    			"            ?ds a dctypes:Dataset ; idot:preferredPrefix ?dataset .\n" + 
    			"            ?version dct:isVersionOf ?ds ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
    			"            FILTER(?dataset = \"%s\")\n" + 
    			"            graph ?graph {\n" + 
    			"                [] a ?classUri ;\n" + 
    			"                   bl:id ?id .\n" +
    			"            }\n" + 
    			"        }\n" + 
    			"        group by ?dataset ?classUri\n" + 
    			"        order by desc(?count)\n" + 
    			"    }\n" + 
    			"    BIND(strafter(str(?classUri),\"http://w3id.org/biolink/vocab/\") as ?class)\n" +
    			"    FILTER(strlen(?class) > 0)\n" + 
    			"}"
    			, dataset);
    	
    	handleRequestResponse(request, response, sparql);
    }
    
    @ApiOperation(value="Returns all instances of a class. Default limit is 1000 instances per page. Use page parameter to load more.")
    @RequestMapping(value = "/{dataset}/{class}"
    	, method = RequestMethod.GET
    	, produces = {"application/json", "application/xml", "text/csv", "text/tsv"})
    public void datasetClass(
    		HttpServletRequest request
    		, HttpServletResponse response
    		, @PathVariable("dataset") String source
    		, @PathVariable("class") String className
    		, @RequestParam(required=false) Long page
    		) throws IOException {
    	String sparql=String.format(PREFIX
				+ "SELECT ?dataset ?class ?id\n" + 
    			"WHERE {\n" + 
    			"    ?ds a dctypes:Dataset ; idot:preferredPrefix ?dataset .\n" + 
    			"    ?version dct:isVersionOf ?ds ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
    			"    FILTER(?dataset = \"%s\")\n" + 
    			"    GRAPH ?graph \n" + 
    			"    {\n" + 
    			"        ?entityUri a ?class .\n" + 
    			"        ?entityUri a bl:%s .\n" + 
    			"        ?entityUri bl:id ?id\n" + 
    			"    }\n" + 
    			"}"
				, source, className);
    	
    	if(page==null || page < 1)
    		page = 1L;
    	
    	sparql += " OFFSET " + ((page - 1L) * LIMIT)
    			+ " LIMIT " + LIMIT;
    	
    	handleRequestResponse(request, response, sparql);
    }
    
    @ApiOperation(value="Loads all properties of a specific instance.")
    @RequestMapping(value = "/{dataset}/{class}/{id}"
    	, method = RequestMethod.GET
    	, produces = {"application/json", "application/xml", "text/csv", "text/tsv"})
    public void sourceClassId(
    		HttpServletRequest request
    		, HttpServletResponse response
    		, @PathVariable("dataset") String source
    		, @PathVariable("class") String className
    		, @PathVariable("id") String id
    		) throws IOException {
    	String sparql=String.format(PREFIX
				+ "SELECT ?dataset ?class ?id ?property ?value\n" + 
				"WHER {\n" + 
				"    ?ds a dctypes:Dataset ; idot:preferredPrefix ?dataset .\n" + 
				"    ?version dct:isVersionOf ?ds ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
				"    FILTER(?dataset = \"%s\")\n" + 
				"    GRAPH ?graph\n" + 
				"    {\n" + 
				"        ?entityUri a bl:%s .\n" + 
				"        ?entityUri a ?classUri .\n" + 
				"        ?entityUri bl:id ?id .\n" + 
				"        ?entityUri ?property ?value .\n" + 
				"        FILTER(?id = \"%s\")\n" + 
				"    }\n" + 
				"    BIND(strafter(str(?classUri),\"http://w3id.org/biolink/vocab/\") as ?class)\n" +
				"}"
				, source, className, id);
    	
    	handleRequestResponse(request, response, sparql);
    }

	private void handleRequestResponse(HttpServletRequest request, HttpServletResponse response, String sparql) throws IOException {
		ResultAs resultAs = ResultAs.fromContentType(request.getHeader("accept"));
		response.setContentType(resultAs.getContentType());
    	rdfRepo.executeSparql(sparql, response.getOutputStream(), resultAs);
	}
    
    
}
