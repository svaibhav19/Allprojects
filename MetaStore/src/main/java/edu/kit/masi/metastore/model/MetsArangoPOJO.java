package edu.kit.masi.metastore.model;


public class MetsArangoPOJO {

	private String xmlData;
	private String mainXmlHandler;
	private String sections;
	private String id;
	
	public String getSections() {
		return sections;
	}

	public void setSections(String sections) {
		this.sections = sections;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMainXmlHandler() {
		return mainXmlHandler;
	}

	public void setMainXmlHandler(String mainXmlHandler) {
		this.mainXmlHandler = mainXmlHandler;
	}

	public String getXmlData() {
		return xmlData;
	}

	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}

}