package org.amalic.servicefromdata;

import javax.servlet.ServletOutputStream;

import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.springframework.stereotype.Component;

@Component
public class RdfRepository {
	private SPARQLRepository repo;
	
	private RdfRepository() {
		repo = new SPARQLRepository("http://graphdb.dumontierlab.com/repositories/ncats-red-kg");
	}
	
	public void executeSparql(String sparql, final ServletOutputStream outputStream, ResultAs resultType) {
		System.out.println(sparql);
		Repositories.tupleQueryNoTransaction(getRepo(), sparql, resultType.getWriter(outputStream));
	}
	
	public SPARQLRepository getRepo() {
		if(!repo.isInitialized())
			repo.initialize();
		return repo;
	}
	
}
