package org.amalic.servicefromdata.repository;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RdfRepository {
	private static final Logger logger = Logger.getLogger(RdfRepository.class.getName());
	
	@Autowired
	private Environment env;
	
	@Value("${default.endpoint}")
	private String defaultEndpoint;
	
	private SPARQLRepository repo;
	
	public void executeSparql(String sparql, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResultAs resultAs = ResultAs.fromContentType(request.getHeader("accept"));
		response.setContentType(resultAs.getContentType());
		executeSparql(sparql, response.getOutputStream(), resultAs);
	}
	
	public void executeSparql(String sparql, final ServletOutputStream outputStream, ResultAs resultType) {
		logger.fine(sparql.replace("\n", " "));
		Repository repo = getRepo();
		try {
			Repositories.tupleQueryNoTransaction(repo, sparql, resultType.getWriter(outputStream));
		} finally {
			repo.getConnection().close();
		}
		
	}
	
	public List<BindingSet> executeSparql(String sparql) {
		 return Repositories.tupleQueryNoTransaction(getRepo(), sparql, iter -> QueryResults.asList(iter));
	}
	
	public SPARQLRepository getRepo() {
		if(repo==null) {
			String endpoint = System.getenv("ENDPOINT");
			if(endpoint == null || endpoint.length()==0)
				endpoint = defaultEndpoint;
			logger.info("ENDPOINT: " + endpoint);
			repo = new SPARQLRepository(endpoint);
		}
		if(!repo.isInitialized()) {
			repo.initialize();
			logger.info("Repository initialized");
		}
		return repo;
	}
	
}
