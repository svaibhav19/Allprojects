package com.test.app;

import java.util.HashSet;
import java.util.Set;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.object.RDFObject;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.Body;
import com.github.anno4j.model.Motivation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.impl.body.TextualBody;
import com.github.anno4j.model.impl.multiplicity.Choice;

public class TestingChoice {

	public static void main(String[] args) {
		Anno4j anno4j;
		try {
			anno4j = new Anno4j();
			Annotation annotation = anno4j.createObject(Annotation.class);
			
			Choice choice = anno4j.createObject(Choice.class);
			
			TextualBody txtBody1 = anno4j.createObject(TextualBody.class);
			Motivation taggingMotivation = MotivationFactory.getTagging(anno4j);
			txtBody1.addPurpose(taggingMotivation);
			txtBody1.setValue("love");
			
			TextualBody txtBody12 = anno4j.createObject(TextualBody.class);
			Motivation taggingMotivation2 = MotivationFactory.getTagging(anno4j);
			txtBody12.addPurpose(taggingMotivation2);
			txtBody12.setValue("love2222");
			
			choice.addItem(txtBody1);
			choice.addItem(txtBody12);
			
			TextualBody txtBody13 = anno4j.createObject(TextualBody.class);
			Motivation taggingMotivation3 = MotivationFactory.getTagging(anno4j);
			txtBody13.addPurpose(taggingMotivation3);
			txtBody13.setValue("love333333");
			
			annotation.addBody(txtBody13);
			annotation.addBody(choice);
			
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
