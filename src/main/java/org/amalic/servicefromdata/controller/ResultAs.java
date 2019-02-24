package org.amalic.servicefromdata.controller;

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
	
	public TupleQueryResultWriter getWriter(OutputStream out) {
		switch(this) {
		case CSV: return new SPARQLResultsCSVWriter(out);
		case TSV: return new SPARQLResultsTSVWriter(out);
		case XML: return new SPARQLResultsXMLWriter(out); 
		case JSON: return new SPARQLResultsJSONWriter(out);
		default: throw new IllegalStateException();
		}
	}

}
