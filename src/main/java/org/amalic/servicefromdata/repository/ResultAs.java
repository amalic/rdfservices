package org.amalic.servicefromdata.repository;

import java.io.OutputStream;

import org.eclipse.rdf4j.query.resultio.TupleQueryResultWriter;
import org.eclipse.rdf4j.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.eclipse.rdf4j.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.eclipse.rdf4j.query.resultio.text.csv.SPARQLResultsCSVWriter;
import org.eclipse.rdf4j.query.resultio.text.tsv.SPARQLResultsTSVWriter;

public enum ResultAs {
	CSV
	, TSV
	, XML
	, JSON;
	
	private final static String CONTENT_TYPE_XML = "application/xml";
	private final static String CONTENT_TYPE_JSON = "application/json";
	private final static String CONTENT_TYPE_CSV = "text/csv";
	private final static String CONTENT_TYPE_TSV = "text/tsv";
	
	private final static String[] CONTENT_TYPES = new String[]{
			CONTENT_TYPE_XML, CONTENT_TYPE_JSON, CONTENT_TYPE_CSV, CONTENT_TYPE_TSV};
	
	public TupleQueryResultWriter getWriter(OutputStream out) {
		switch(this) {
		case CSV: return new SPARQLResultsCSVWriter(out);
		case TSV: return new SPARQLResultsTSVWriter(out);
		case XML: return new SPARQLResultsXMLWriter(out); 
		case JSON: return new SPARQLResultsJSONWriter(out);
		default: throw new IllegalStateException();
		}
	}

	public static ResultAs fromContentType(String accept) {
		switch(accept) {
		case CONTENT_TYPE_XML: return XML;
		case CONTENT_TYPE_JSON: return JSON;
		case CONTENT_TYPE_CSV: return CSV;
		case CONTENT_TYPE_TSV: return TSV;
		default: throw new IllegalStateException("Unknown accept header. Try one of these " + CONTENT_TYPES.toString());
		}
	}
	
	public String getContentType() {
		switch(this) {
		case CSV: return CONTENT_TYPE_CSV;
		case TSV: return CONTENT_TYPE_TSV;
		case XML: return CONTENT_TYPE_XML; 
		case JSON: return CONTENT_TYPE_JSON;
		default: throw new IllegalStateException();
		}
	}

}
