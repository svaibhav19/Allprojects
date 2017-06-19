package com.test.complete;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

public class MainAppRunner {

	public static void main(String[] args) {

		CompleteExampleAnnotation completeExg = new CompleteExampleAnnotation();
		try {
			completeExg.initliseObj();
			completeExg.startBuildingExample();
			completeExg.printJson();
			completeExg.printRdf();
			completeExg.printTurtle();
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
