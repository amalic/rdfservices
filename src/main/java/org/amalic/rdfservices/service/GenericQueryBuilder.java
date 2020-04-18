package org.amalic.rdfservices.service;

public class GenericQueryBuilder extends AbstractQueryBuilder {

	public static String graphs() {
		return "SELECT (?g as ?Graph) (count(?p) as ?Statements)\n" + 
				"WHERE {\n" + 
				"    GRAPH ?g { [] ?p [] }\n" + 
				"} \n" + 
				"group by ?g\n" + 
				"order by ?g\n";
	}
	
	public static String getClasses(String graph) {
		return String.format( 
				"SELECT (?c as ?Class) (count(?c) as ?Instances)\n" + 
				"WHERE {\n" + 
				"    GRAPH <%s> \n" + 
				"    { [] a ?c }\n" + 
				"} \n" + 
				"group by ?c\n" + 
				"order by ?c"
				, graph
				);
	}
	
	public static String listInstances(String graph, String className, Long page, Long limit) {
		return String.format(
				"select (?s as ?Instance) where { \n" + 
				"    graph <%s> {\n" + 
				"		?s a <%s> .\n" + 
				"	}\n" + 
				"}" 
				, graph
				, className) 
			+ paginate(page, limit);
	}
	
	public static String getPropertiesOfInstance(String graph, String className, String id) {
		return String.format(
				"select (?p as ?Property) (?o as ?Value) where { \n" + 
				"    graph <%s> {\n" + 
				"		<%s> a <%s> ;\n" + 
				"			?p ?o.\n" + 
				"	}\n" + 
				"}\n" + 
				"order by ?p"
				, graph, id, className);
	}
	
}
