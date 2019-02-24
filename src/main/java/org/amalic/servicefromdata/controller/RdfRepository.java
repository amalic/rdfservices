package org.amalic.servicefromdata.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RdfRepository {
	private static final Logger logger = Logger.getLogger(RdfRepository.class.getName());
	@Autowired private Environment env;
	
	private SPARQLRepository repo;
	
	private RdfRepository() {
		String endpoint = System.getenv("ENDPOINT");
		if(endpoint == null || endpoint.length()==0)
			endpoint = env.getProperty("default-endpoint");
		logger.info("ENDPOINT: " + endpoint);
		repo = new SPARQLRepository(endpoint);
	}
	
	public void executeSparql(String sparql, final ServletOutputStream outputStream, ResultAs resultType) {
		logger.fine(sparql.replace("\n", " "));
		Repositories.tupleQueryNoTransaction(getRepo(), sparql, resultType.getWriter(outputStream));
	}
	
	public List<BindingSet> executeSparql(String sparql) {
		 return Repositories.tupleQueryNoTransaction(getRepo(), sparql, iter -> QueryResults.asList(iter));
	}
	
	public SPARQLRepository getRepo() {
		if(!repo.isInitialized())
			repo.initialize();
		return repo;
	}

	public void executeSparql(String sparql, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResultAs resultAs = ResultAs.fromContentType(request.getHeader("accept"));
		response.setContentType(resultAs.getContentType());
    	executeSparql(sparql, response.getOutputStream(), resultAs);
	}
	
}
