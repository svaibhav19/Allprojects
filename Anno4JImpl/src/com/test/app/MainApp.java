package com.test.app;

import java.util.HashSet;
import java.util.Set;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.Body;
import com.github.anno4j.model.Motivation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.impl.agent.Person;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.body.TextualBody;

/**
 * 
 * @author VB
 *
 */
public class MainApp {

	public static void main(String[] args) {
		try {
			Anno4j anno4j = new Anno4j();
//			anno4j.setRepository(new SPARQLRepository("http://localhost:3030/testao/"));
			// anno4j.setIdGenerator(new IDGeneratorAnno4jURN());
			Annotation annotation = anno4j.createObject(Annotation.class);
			// formerly annotatedAt
			annotation.setCreated("2014-09-28T12:00:00Z");
			// formerly serializedAt
			annotation.setGenerated("2013-02-04T12:00:00Z");
			// creating motivatation why it was created
			Motivation commenting = MotivationFactory.getCommenting(anno4j);
			annotation.addMotivation(commenting);

			// creating creator agent of the annotations by human
			Person person = anno4j.createObject(Person.class);
			person.setName("A. Person");
//			person.setOpenID("http://example.org/agent1/openID1");
			annotation.setCreator(person);

			// created by the software agent for the annotation
			Software software = anno4j.createObject(Software.class);
			software.setName("Code v2.1");
//			software.setHomepage("http://example.org/agent2/homepage1");
			annotation.setGenerator(software);

			// Creating the body
//			TextAnnotationBody body = anno4j.createObject(TextAnnotationBody.class);
//			body.setFormat("text/plain");
//		    body.setValue("One of my favourite cities");
//		    body.setLanguage("en");
//		    
//		    Set<Body> bodySet = new HashSet<Body>();
//		    bodySet.add(body);
//		    // Adding the body to the annotation object
//		    annotation.setBodies(bodySet);
			//creating bodies 
			TextualBody body = anno4j.createObject(TextualBody.class);
			
//			body.setFormat("text/plain");
//		    body.setValue("One of my favourite cities");
//		    Set<String> lang = new HashSet<String>();
//		    lang.add("en");
//		    body.setLanguages(lang);
//		    Set<Body> bodies = new HashSet<Body>();
//		    bodies.add(body);
//		    annotation.setBodies(bodies);
			System.out.println(annotation.getTriples(RDFFormat.JSONLD));
			System.out.println("--------------");
			System.out.println(annotation.getTriples(RDFFormat.RDFXML));
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}
}
