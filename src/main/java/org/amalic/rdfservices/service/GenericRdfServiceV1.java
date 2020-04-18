package org.amalic.rdfservices.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.amalic.rdfservices.repository.RdfRepository;
import org.amalic.rdfservices.repository.ResultAs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/generic/v1")
public class GenericRdfServiceV1 {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GenericRdfServiceV1.class.getName());
    
    @Autowired
	private RdfRepository repository;
    
    @GetMapping(value = "/getGraphs")
    @Operation(summary = "This api call returns all graphs and statement count."
		, responses = { 
			@ApiResponse(
    			content = {
					@Content(mediaType = ResultAs.CONTENT_TYPE_CSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_TSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_JSON)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_XML)
	})})
    public void getGraphs(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	repository.handleApiCall(GenericRdfServiceQueryBuilder.graphs(), request, response);
    }
    
    @GetMapping(value = "/getClasses")
    @Operation(summary = "Returns a list of classes and instance count."
		, responses = { 
			@ApiResponse(
				content = {
					@Content(mediaType = ResultAs.CONTENT_TYPE_CSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_TSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_JSON)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_XML)
	})})
    public void getClasses(HttpServletRequest request, HttpServletResponse response
    		, @RequestParam(required = false) String graph
    		) throws IOException {
    	repository.handleApiCall(GenericRdfServiceQueryBuilder.getClasses(graph), request, response);
    }
    
    @GetMapping(value = "/listInstances")
    @Operation(summary = "Returns all instances."
		, responses = { 
			@ApiResponse(
				content = {
					@Content(mediaType = ResultAs.CONTENT_TYPE_CSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_TSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_JSON)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_XML)
	})})
    public void listInstances(HttpServletRequest request, HttpServletResponse response
    		, @RequestParam(required = false) String graph
    		, @RequestParam(required = false) String className
    		, @RequestParam(required = false) Long page
    		, @RequestParam(required = false) Long limit
    		) throws IOException {
    	repository.handleApiCall(GenericRdfServiceQueryBuilder.listInstances(graph, className, page, limit), request, response);
    }
    
    @GetMapping(value = "/getPropertiesOfInstance")
    @Operation(
    	summary = "Loads all properties and values of a specific instance."
	    , responses = { 
	    	@ApiResponse(
	    		content = {
		    		@Content(mediaType = ResultAs.CONTENT_TYPE_CSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_TSV)
		    		, @Content(mediaType = ResultAs.CONTENT_TYPE_JSON)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_XML)
    })})
    public void getPropertiesOfInstance(HttpServletRequest request, HttpServletResponse response
    		, @RequestParam(required = false) String graph
    		, @RequestParam(required = false) String className
    		, @RequestParam String id
    		) throws IOException {
    	repository.handleApiCall(GenericRdfServiceQueryBuilder.getPropertiesOfInstance(graph, className, id), request, response);
    }
    
    @PostMapping(value = "/executeSparql", consumes =  "application/sparql-query")
    @Operation(
    	summary = "Executes a SPARQL statement"
	    , responses = { 
	    	@ApiResponse(
	    		content = {
					@Content(mediaType = ResultAs.CONTENT_TYPE_CSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_TSV)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_JSON)
					, @Content(mediaType = ResultAs.CONTENT_TYPE_XML)
	})})
    public void executeSparql(HttpServletRequest request, HttpServletResponse response
    		, @RequestBody String sparql
    		, @RequestParam(required = false) Long page
    		, @RequestParam(required = false) Long limit
    		) throws IOException {
    	repository.handleApiCall(GenericRdfServiceQueryBuilder.executeSparql(sparql, page, limit), request, response);
    }
    
    
    
}
