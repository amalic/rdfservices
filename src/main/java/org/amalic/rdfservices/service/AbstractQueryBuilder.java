package org.amalic.rdfservices.service;

public class AbstractQueryBuilder {
	public static final Long MAX_LIMIT = 100000L;
	public static final Long DEFAULT_LIMIT = 100L;
	
	public static String checkGraph(String innerSparql, String graph) {
		if(graph!=null && graph.length()>0)
			return String.format(" graph <%s> {", graph) + innerSparql + "}";
		else
			return innerSparql;
	}
	
	static String paginate(String sparql, Long page, Long limit) {
		return sparql.replaceAll("(?i)(\\s+(limit|offset)\\s+\\d+\\s*)+$", "") + paginate(page, limit);
	}

	protected static String paginate(Long page, Long limit) {
		if (limit == null || limit > MAX_LIMIT || limit<1L)
			limit = DEFAULT_LIMIT;
		return (page!=null && page > 1L ? " OFFSET " + ((page - 1L) * limit) : "")
				+ " LIMIT " + limit;
	}

}