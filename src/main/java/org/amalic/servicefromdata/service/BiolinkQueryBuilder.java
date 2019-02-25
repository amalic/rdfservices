package org.amalic.servicefromdata.service;

public class BiolinkQueryBuilder {
	
	public static final Long LIMIT = 1000L;
	
	public static final String PREFIXES = 
    		"PREFIX bl: <http://w3id.org/biolink/vocab/>\n"
			+ "PREFIX void: <http://rdfs.org/ns/void#>\n"
			+ "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n"
			+ "PREFIX dct: <http://purl.org/dc/terms/>\n"
			+ "PREFIX idot: <http://identifiers.org/idot/>\n"
			+ "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n";

	public static String datasets() {
		return PREFIXES 
				+ "SELECT ?dataset\n" 
				+ "WHERE {\n" 
				+ "  ?ds a dctypes:Dataset ;\n" 
				+ "  idot:preferredPrefix ?dataset .\n" 
				+ "  ?version dct:isVersionOf ?ds ;\n" 
				+ "  dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . \n" + 
	    		"}\n";
	}
	
	private static final String GRAPH_FROM_DATASET_PART = "?ds a dctypes:Dataset ; idot:preferredPrefix ?dataset .\n" 
					+ "?version dct:isVersionOf ?ds ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] .\n" 
					+ "FILTER(?dataset = \"%s\")\n";
	
	public static String classes(String dataset) {
		return String.format(PREFIXES 
				+ "SELECT ?dataset ?class ?count\n" 
				+ "WHERE {\n" 
				+ "  {\n" 
				+ "    SELECT ?dataset ?classUri (count(?classUri) as ?count)  \n" 
				+ "    WHERE {\n"
				+ GRAPH_FROM_DATASET_PART
				+ "      graph ?graph {\n" 
				+ "        [] a ?classUri ;\n" 
				+ "        bl:id ?id .\n" 
				+ "      }\n" 
				+ "    }\n" 
				+ "    group by ?dataset ?classUri\n" 
				+ "    order by desc(?count)\n" 
				+ "  }\n" 
				+ "  BIND(strafter(str(?classUri),\"http://w3id.org/biolink/vocab/\") as ?class)\n" 
				+ "  FILTER(strlen(?class) > 0)\n" 
				+ "}"
    			, dataset);
	}
	
	public static String datasetClass(String dataset, String className, Long page) {
		String query = String.format(PREFIXES 
				+ "SELECT ?dataset ?class ?id\n" 
				+ "WHERE {\n" 
				+ GRAPH_FROM_DATASET_PART 
				+ "  GRAPH ?graph {\n" 
				+ "    ?entityUri a ?class .\n" 
				+ "    ?entityUri a bl:%s .\n" 
				+ "    ?entityUri bl:id ?id\n" 
				+ "}}" 
				, dataset, className);
		
		if(page==null || page < 1L)
    		page = 1L;
    	
    	query += (page > 1 ? " OFFSET " + ((page - 1L) * LIMIT) : "")
    			+ " LIMIT " + LIMIT;
		return query;
	}
	
	public static String datasetClassId(String dataset, String className, String id) {
		return String.format(PREFIXES 
				+ "SELECT ?dataset ?class ?id ?property ?value\n" 
				+ "WHERE {\n" 
				+ GRAPH_FROM_DATASET_PART
				+ "  GRAPH ?graph {\n" 
				+ "    ?entityUri a bl:%s .\n" 
				+ "    ?entityUri a ?classUri .\n" 
				+ "    ?entityUri bl:id ?id .\n" 
				+ "    ?entityUri ?property ?value .\n" 
				+ "    FILTER(?id = \"%s\")\n" 
				+ "  }\n" 
				+ "  BIND(strafter(str(?classUri),\"http://w3id.org/biolink/vocab/\") as ?class)\n" 
				+ "}"
				, dataset, className, id);
	}

}
