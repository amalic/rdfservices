package org.amalic.servicefromdata.controller;

import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;

import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.springframework.stereotype.Component;

@Component
public class RdfRepository {
	private static final Logger logger = Logger.getLogger(RdfRepository.class.getName());
	
	private SPARQLRepository repo;
	
	private RdfRepository() {
		repo = new SPARQLRepository("http://graphdb.dumontierlab.com/repositories/ncats-red-kg");
	}
	
	public void executeSparql(String sparql, final ServletOutputStream outputStream, ResultAs resultType) {
		logger.info(sparql.replace("\n", " "));
		Repositories.tupleQueryNoTransaction(getRepo(), sparql, resultType.getWriter(outputStream));
	}
	
	public SPARQLRepository getRepo() {
		if(!repo.isInitialized())
			repo.initialize();
		return repo;
	}
	
}
