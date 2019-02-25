package org.amalic.servicefromdata.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.amalic.servicefromdata.repository.RdfRepository;
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
	private RdfRepository rdfRepo;
    
    @RequestMapping(value = "/datasets"
    	, method = RequestMethod.GET
    	, produces = {"application/xml", "application/json", "text/csv", "text/tsv"})
    @ApiOperation(value="This api call returns all datasets, which can be used as input for other services. Note that the first line in csv is the header.")
    public void datasets(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	rdfRepo.handleApiCall(BiolinkQueryBuilder.datasets(), request, response);
    }
    
    @RequestMapping(value = "/query/{dataset}"
	    , method = RequestMethod.GET
	    , produces = {"application/xml", "application/json", "text/csv", "text/tsv"})
    @ApiOperation(value="This all classes for this particular data-set with instances having an id.")
    public void classes(HttpServletRequest request, HttpServletResponse response
    		, @PathVariable String dataset
    		) throws IOException {
    	rdfRepo.handleApiCall(BiolinkQueryBuilder.classes(dataset), request, response);
    }
    
    @RequestMapping(value = "/query/{dataset}/{class}"
    	, method = RequestMethod.GET
    	, produces = {"application/xml", "application/json", "text/csv", "text/tsv"})
    @ApiOperation(value="Returns all instances of a class. Default limit is 1000 instances per page. Use page parameter to load more.")
    public void datasetClass(HttpServletRequest request, HttpServletResponse response
    		, @PathVariable String dataset
    		, @PathVariable("class") String className
    		, @RequestParam(required=false) Long page
    		) throws IOException {
    	rdfRepo.handleApiCall(BiolinkQueryBuilder.datasetClass(dataset, className, page), request, response);
    }
    
    @RequestMapping(value = "/query/{dataset}/{class}/{id}"
    	, method = RequestMethod.GET
    	, produces = {"application/xml", "application/json", "text/csv", "text/tsv"})
    @ApiOperation(value="Loads all properties of a specific instance.")
    public void datasetClassId(HttpServletRequest request, HttpServletResponse response
    		, @PathVariable String dataset
    		, @PathVariable("class") String className
    		, @PathVariable String id
    		) throws IOException {
    	rdfRepo.handleApiCall(BiolinkQueryBuilder.datasetClassId(dataset, className, id), request, response);
    }

}
