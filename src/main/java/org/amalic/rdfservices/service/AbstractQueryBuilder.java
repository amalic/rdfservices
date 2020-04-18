package org.amalic.rdfservices.service;

public class AbstractQueryBuilder {
	public static final Long LIMIT = 1000L;
	
	public static String checkGraph(String innerSparql, String graph) {
		if(graph!=null && graph.length()>0)
			return String.format(" graph <%s> {", graph) + innerSparql + "}";
		else
			return innerSparql;
	}
	
	static String paginate(String sparql, Long page, Long limit) {
		// TODO:
		return sparql + paginate(page, limit);
	}

	protected static String paginate(Long page, Long limit) {
		if (limit == null || limit > LIMIT || limit<1L)
			limit = LIMIT;
		return (page!=null && page > 1L ? " OFFSET " + ((page - 1L) * limit) : "")
				+ " LIMIT " + limit;
	}

}