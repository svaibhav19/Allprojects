package com.test.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.io.ObjectParser;
import com.github.anno4j.model.Annotation;

public class JsonLDToRdf {

	public static void main(String[] args) {
		CompJSONLD jsonld = new CompJSONLD();
		String TURTLE = "@prefix oa: <http://www.w3.org/ns/oa#> ." + "@prefix ex: <http://www.example.com/ns#> ."
				+ "@prefix dctypes: <http://purl.org/dc/dcmitype/> ."
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ." +

				"ex:anno1 a oa:Annotation ;" + "   oa:hasBody ex:body1 ;" + "   oa:hasTarget ex:target1 .";

		String turtle = "<anno1> a oa:Annotation ;" + "oa:hasBody <body1> ;" + "oa:hasTarget <sptarget1> ." +
				"<sptarget1> a oa:SpecificResource ;" + "oa:hasState <state1> ;" + "oa:hasSource <target1> ." +
				"<state1> a oa:TimeState ;" + "oa:cachedSource <copy1> ;" + "oa:when \"2012-07-20T13:30:00Z\" .";
		
		URL url = null;
		try {
			url = new URL("http://example.com/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ObjectParser objectParser = null;
		try {
			objectParser = new ObjectParser();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		}
		List<Annotation> annotations = objectParser.parse(jsonld.getJsonLd2(), url, RDFFormat.JSONLD);
		System.out.println(annotations.get(0).getTriples(RDFFormat.RDFXML));
	}
}
