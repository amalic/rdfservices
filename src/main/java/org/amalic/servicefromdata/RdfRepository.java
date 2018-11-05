package org.amalic.servicefromdata;

import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.springframework.stereotype.Component;

@Component
public class RdfRepository {
	private SPARQLRepository repo;
	
	private RdfRepository() {
		repo = new SPARQLRepository("http://node000002.cluster.ids.unimaas.nl/repositories/ncats-red-kg", "http://node000002.cluster.ids.unimaas.nl/repositories/ncats-red-kg/statements");
		repo.setUsernameAndPassword("import_user", "test");
	}
	
	public String executeSparql(String sparql) {
		List<BindingSet> results = Repositories.tupleQuery(getRepo(), sparql, r -> QueryResults.asList(r));

		return results.toString();
	}
	
	public SPARQLRepository getRepo() {
		if(!repo.isInitialized())
			repo.initialize();
		return repo;
	}
	
}
