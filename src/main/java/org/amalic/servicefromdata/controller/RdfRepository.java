package org.amalic.servicefromdata.controller;

import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;

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
	
	public SPARQLRepository getRepo() {
		if(!repo.isInitialized())
			repo.initialize();
		return repo;
	}
	
}
