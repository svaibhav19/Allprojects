package com.test.app;

import org.openrdf.model.impl.EmptyModel;
import org.openrdf.repository.sparql.SPARQLRepository;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.agent.Person;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.selector.TextPositionSelector;
import com.github.anno4j.model.impl.targets.SpecificResource;


/**
 * Test case to implement the example annotation at {@link }http://www.w3.org/TR/2014/WD-annotation-model-20141211/#complete-example}.
 */
public class ExampleTest {

    private Anno4j anno4j;

    public void setUp() throws Exception {
        this.anno4j = new Anno4j();
//        this.anno4j.setRepository(new SPARQLRepository("http://localhost:3030/test/"));
    }

    public void exampleTest() throws Exception {

        // Create the base annotation
        Annotation annotation = anno4j.createObject(Annotation.class);
        annotation.setCreated("2014-09-28T12:00:00Z");
        annotation.setGenerated("2013-02-04T12:00:00Z");
        annotation.addMotivation((MotivationFactory.getCommenting(anno4j)));

        // Create the person agent for the annotation
        Person person = anno4j.createObject(Person.class);
        person.setName("A. Person");
        person.setOpenID("http://example.org/agent1/openID1");

        annotation.setCreator(person);

        // Create the software agent for the annotation
        Software software = anno4j.createObject(Software.class);
        software.setName("Code v2.1");
        software.setHomepage("http://example.org/agent2/homepage1");

        annotation.setGenerator(software);

        // Create the body
        TextAnnotationBody body = anno4j.createObject(TextAnnotationBody.class);
        body.setFormat("text/plain");
        body.setValue("One of my favourite cities");
        body.setLanguage("en");
        annotation.addBody(body);

        // Create the selector
        SpecificResource specificResource = anno4j.createObject(SpecificResource.class);

        TextPositionSelector textPositionSelector = anno4j.createObject(TextPositionSelector.class);
        textPositionSelector.setStart(4096);
        textPositionSelector.setEnd(4104);

        specificResource.setSelector(textPositionSelector);

        // Create the actual target
        ResourceObject source = anno4j.createObject(ResourceObject.class);
        source.setResourceAsString("http://example.org/source1");
        specificResource.setSource(source);

        annotation.addTarget(specificResource);

    }
    public static void main(String[] args) {
		ExampleTest emp1 = new ExampleTest();
		try {
			emp1.setUp();
			emp1.exampleTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}