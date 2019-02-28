package org.amalic.servicefromdata.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.amalic.servicefromdata.repository.RdfRepository;
import org.amalic.servicefromdata.repository.ResultAs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/biolink/v1")
public class BiolinkService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BiolinkService.class.getName());
    
    @Autowired
	private RdfRepository repository;
    
    @RequestMapping(value = "/datasets"
    	, method = RequestMethod.GET
		, produces = {ResultAs.CONTENT_TYPE_XML, ResultAs.CONTENT_TYPE_JSON, ResultAs.CONTENT_TYPE_CSV, ResultAs.CONTENT_TYPE_TSV})
    @ApiOperation(value="This api call returns all datasets, which can be used as input for other services. Note that the first line in csv is the header.")
    public void datasets(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	repository.handleApiCall(BiolinkQueryBuilder.datasets(), request, response);
    }
    
    @RequestMapping(value = "/query/{dataset}"
	    , method = RequestMethod.GET
	    , produces = {ResultAs.CONTENT_TYPE_XML, ResultAs.CONTENT_TYPE_JSON, ResultAs.CONTENT_TYPE_CSV, ResultAs.CONTENT_TYPE_TSV})
    @ApiOperation(value="This all classes for this particular data-set with instances having an id.")
    public void classes(HttpServletRequest request, HttpServletResponse response
    		, @PathVariable String dataset
    		) throws IOException {
    	repository.handleApiCall(BiolinkQueryBuilder.classes(dataset), request, response);
    }
    
    @RequestMapping(value = "/query/{dataset}/{class}"
    	, method = RequestMethod.GET
    	, produces = {ResultAs.CONTENT_TYPE_XML, ResultAs.CONTENT_TYPE_JSON, ResultAs.CONTENT_TYPE_CSV, ResultAs.CONTENT_TYPE_TSV})
    @ApiOperation(value="Returns all instances of a class. Default and maximum limit is 1000 instances per page. Use page parameter to load more.")
    public void datasetClass(HttpServletRequest request, HttpServletResponse response
    		, @PathVariable String dataset
    		, @PathVariable("class") String className
    		, @RequestParam(required=false) Long page
    		, @RequestParam(required=false) Long limit
     		) throws IOException {
    	repository.handleApiCall(BiolinkQueryBuilder.datasetClass(dataset, className, page, limit), request, response);
    }
    
    @RequestMapping(value = "/query/{dataset}/{class}/{id}"
    	, method = RequestMethod.GET
    	, produces = {ResultAs.CONTENT_TYPE_XML, ResultAs.CONTENT_TYPE_JSON, ResultAs.CONTENT_TYPE_CSV, ResultAs.CONTENT_TYPE_TSV})
    @ApiOperation(value="Loads all properties of a specific instance.")
    public void datasetClassId(HttpServletRequest request, HttpServletResponse response
    		, @PathVariable String dataset
    		, @PathVariable("class") String className
    		, @PathVariable String id
    		) throws IOException {
    	repository.handleApiCall(BiolinkQueryBuilder.datasetClassId(dataset, className, id), request, response);
    }
    
}
