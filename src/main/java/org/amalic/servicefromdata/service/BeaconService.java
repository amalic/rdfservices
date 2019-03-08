package org.amalic.servicefromdata.service;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.amalic.servicefromdata.repository.RdfRepository;
import org.amalic.servicefromdata.repository.ResultAs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/beacon/v1.2.0/")
public class BeaconService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BeaconService.class.getName());
	
	@SuppressWarnings("unused")
	@Autowired
	private RdfRepository repository;
	
	@RequestMapping(value = "/categories"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
			, notes = "Get a list of concept categories and number of their concept instances documented"
			+ " by the knowledge source. These types should be mapped onto the Translator-endorsed Biolink Model"
			+ " concept type classes with local types, explicitly added as mappings to the Biolink Model YAML"
			+ " file. A frequency of -1 indicates the category can exist, but the count is unknown."
			)
	public String categories(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"categories\":\"tbd\"}";
	}
	
	@RequestMapping(value = "/predicates"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
			, notes = "Get a list of predicates used in statements issued by the knowledge source")
	public String predicates(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"predicates\":\"tbd\"}";
	}
	
	@RequestMapping(value = "/kmap"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
	, notes = "Get a high level knowledge map of the all the beacons by subject semantic type, predicate and"
			+ " semantic object type")
	public String kmap(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"kmap\":\"tbd\"}";
	}
	
	@RequestMapping(value = "/concepts"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
	, notes = "Retrieves a list of whose concept in the beacon knowledge base with names and/or synonyms" 
			+ " matching a set of keywords or substrings. The results returned should generally be returned in" 
			+ " order of the quality of the match, that is, the highest ranked concepts should exactly match"
			+ " the most keywords, in the same order as the keywords were given. Lower quality hits with fewer" 
			+ " keyword matches or out-of-order keyword matches, should be returned lower in the list.")
	public String concepts(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(required=false) String[] keywords
			, @RequestParam(required=false) String[] categories
			, @RequestParam int offset
			, @RequestParam int size
			) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"keywords\":\"" + Arrays.toString(keywords) + "\""
				+ ", \"categories\":\"" + Arrays.toString(categories) + "\""
				+ ", \"offset\":\"" + offset + "\""
				+ ", \"size\":\"" + size + "\""
				+ "}";
	}
	
	@RequestMapping(value = "/concepts/{concept_id}"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
	, notes = "Retrieves details for a specified concepts in the system, as specified by a (url-encoded) CURIE"
			+ " identifier of a concept known the given knowledge source.")
	public String conceptById(HttpServletRequest request, HttpServletResponse response
			, @PathVariable(value="concept_id") String conceptId 
			) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"concept_id\":\"" + conceptId + "\"}";
	}
	
	@RequestMapping(value = "/exactmatches"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
	, notes = "Given an input array of CURIE identifiers of known exactly matched concepts sensa-SKOS, retrieves"
			+ " the list of CURIE identifiers of additional concepts that are deemed by the given knowledge source"
			+ " to be exact matches to one or more of the input concepts plus whichever concept identifiers from"
			+ " the input list were specifically matched to these additional concepts, thus giving the whole known"
			+ " set of equivalent concepts known to this particular knowledge source. If an empty set is returned,"
			+ " the it can be assumed that the given knowledge source does not know of any new equivalent concepts"
			+ " matching the input set. The caller of this endpoint can then decide whether or not to treat its"
			+ " input identifiers as its own equivalent set.")
	public String exactmatches(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="c") String[] curies 
			) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"concept_id\":\"" + Arrays.toString(curies) + "\"}";
	}
	
	@RequestMapping(value = "/statements"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
	, notes = "Given a constrained set of some CURIE-encoded 's' ('source') concept identifiers, categories and/or"
			+ " keywords (to match in the concept name or description), retrieves a list of relationship statements"
			+ " where either the subject or the object concept matches any of the input source concepts provided."
			+ " Optionally, a set of some 't' ('target') concept identifiers, categories and/or keywords (to match"
			+ " in the concept name or description) may also be given, in which case a member of the 't' concept set"
			+ " should matchthe concept opposite an 's' concept in the statement. That is, if the 's' concept matches"
			+ " a subject, then the 't' concept should match the object of a given statement (or vice versa).")
	public String statements(HttpServletRequest request, HttpServletResponse response
			, @RequestParam(value="s", required=false) String[] sourceCuries
			, @RequestParam(value="s_keywords", required=false) String[] sourceKeywords
			, @RequestParam(value="s_categories", required=false) String[] sourceCategories
			, @RequestParam(value="edge_label", required=false) String edgeLabel
			, @RequestParam(value="relation", required=false) String relation
			, @RequestParam(value="t", required=false) String[] targetCuries
			, @RequestParam(value="t_keywords", required=false) String[] targetKeywords
			, @RequestParam(value="t_categories", required=false) String[] targetCategories
			, @RequestParam int offset
			, @RequestParam int size
			) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"s\":\"" + Arrays.toString(sourceCuries) + "\""
				+ ", \"s_keywords\":\"" + Arrays.toString(sourceKeywords) + "\""
				+ ", \"s_categories\":\"" + Arrays.toString(sourceCategories) + "\""
				+ ", \"edge_label\":\"" + edgeLabel + "\""
				+ ", \"relation\":\"" + relation + "\""
				+ ", \"t\":\"" + Arrays.toString(targetCuries) + "\""
				+ ", \"t_keywords\":\"" + Arrays.toString(targetKeywords) + "\""
				+ ", \"t_categories\":\"" + Arrays.toString(targetCategories) + "\""
				+ ", \"offset\":\"" + offset + "\""
				+ ", \"size\":\"" + size + "\""
				+ "}";
	}
	
	@RequestMapping(value = "/statements/{statement_id}"
			, method = RequestMethod.GET
			, produces = ResultAs.CONTENT_TYPE_JSON)
	@ApiOperation(value = " "
	, notes = "Retrieves a details relating to a specified concept-relationship statement include 'is_defined_by and"
			+ " 'provided_by' provenance; extended edge properties exported as tag = value; and any associated"
			+ " annotations (publications, etc.) cited as evidence for the given statement.")
	public String statementsById(HttpServletRequest request, HttpServletResponse response
			, @PathVariable(value="statement_id") String statementId
			, @RequestParam String[] keywords
			, @RequestParam int offset
			, @RequestParam int size
			) {
		response.setContentType(ResultAs.JSON.getContentType());;
		return "{\"statement_id\":\"" + statementId + "\""
				+ ", \"keywords\":\"" + Arrays.toString(keywords) + "\""
				+ ", \"offset\":\"" + offset + "\""
				+ ", \"size\":\"" + size + "\""
				+ "}";
	}
	
}
