package com.test.app;

public class CompJSONLD {

	private String jsonLd = "{" + "\"@context\": \"http://www.w3.org/ns/anno.jsonld\","
			+ "\"id\": \"http://example.org/anno38\"," + "\"type\": \"Annotation\"," + "\"motivation\": \"commenting\","
			+ "\"creator\": {" + "  \"id\": \"http://example.org/user1\"," + "  \"type\": \"Person\","
			+ "  \"name\": \"A. Person\"," + "  \"nickname\": \"user1\"" + "},"
			+ "\"created\": \"2015-10-13T13:00:00Z\"," + "\"generator\": {"
			+ "  \"id\": \"http://example.org/client1\"," + "  \"type\": \"Software\"," + "  \"name\": \"Code v2.1\","
			+ "  \"homepage\": \"http://example.org/homepage1\"" + "}," + "\"generated\": \"2015-10-14T15:13:28Z\","
			+ "\"stylesheet\": {" + "  \"id\": \"http://example.org/stylesheet1\"," + "  \"type\": \"CssStylesheet\""
			+ "}," + "\"body\": [" + "{" + " \"type\": \"TextualBody\"," + " \"purpose\": \"tagging\","
			+ " \"value\": \"love\"" + "}," + "{" + "\"type\": \"Choice\"," + "\"items\": [" + "{"
			+ "  \"type\": \"TextualBody\"," + "  \"purpose\": \"describing\","
			+ "  \"value\": \"I really love this particular bit of text in this XML. No really.\","
			+ "  \"format\": \"text/plain\"," + "  \"language\": \"en\","
			+ "  \"creator\": \"http://example.org/user1\"" + "}," + "{" + "\"type\": \"SpecificResource\","
			+ "\"purpose\": \"describing\"," + "\"source\": {" + "\"id\": \"http://example.org/comment1\","
			+ "\"type\": \"Audio\"," + " \"format\": \"audio/mpeg\"," + "  \"language\": \"de\"," + "   \"creator\": {"
			+ "      \"id\": \"http://example.org/user2\"," + "       \"type\": \"Person\"" + "      }" + "     }"
			+ "    }" + "   ]" + " }" + "]," + "\"target\": {" + "\"type\": \"SpecificResource\","
			+ "\"styleClass\": \"mystyle\"," + "\"source\": \"http://example.com/document1\"," + "\"state\": [" + "{"
			+ "\"type\": \"HttpRequestState\"," + "\"value\": \"Accept: application/xml\"," + "\"refinedBy\": {"
			+ "  \"type\": \"TimeState\"," + "   \"sourceDate\": \"2015-09-25T12:00:00Z\"" + "  }" + " }" + "],"
			+ "\"selector\": {" + "\"type\": \"FragmentSelector\","
			+ "\"value\": \"xpointer(/doc/body/section[2]/para[1])\"," + "\"refinedBy\": {"
			+ "   \"type\": \"TextPositionSelector\"," + "    \"start\": 6," + "     \"end\": 27" + "    }" + "   }"
			+ "  }" + "}";
	
	private String jsonLd2="[ {"+
			"  \"@id\" : \"urn:anno4j:5d7b260d-bea5-499b-93d0-828f61f3cf48\","+
			"  \"@type\" : [ \"http://www.w3.org/ns/oa#Annotation\" ],"+
			"  \"http://purl.org/dc/terms/created\" : [ {"+
			"    \"@value\" : \"2014-09-28T12:00:00Z\""+
			"  } ],"+
			"  \"http://purl.org/dc/terms/creator\" : [ {"+
			"    \"@id\" : \"urn:anno4j:a8c40ef0-bd61-4a99-9ff3-e098eb448f3d\""+
			"  } ],"+
			"  \"http://purl.org/dc/terms/issued\" : [ {"+
			"    \"@value\" : \"2013-02-04T12:00:00Z\""+
			"  } ],"+
			"  \"http://www.w3.org/ns/activitystreams#generator\" : [ {"+
			"    \"@id\" : \"urn:anno4j:cf0dc208-cd16-4104-8b34-a53d2ed6b469\""+
			"  } ],"+
			"  \"http://www.w3.org/ns/oa#motivatedBy\" : [ {"+
			"    \"@id\" : \"urn:anno4j:828ee6e7-3bb7-466a-b520-2b1fcf4bc39e\""+
			"  } ]"+
			"}, {"+
			"  \"@id\" : \"urn:anno4j:828ee6e7-3bb7-466a-b520-2b1fcf4bc39e\","+
			"  \"@type\" : [ \"http://www.w3.org/ns/oa#Motivation\" ]"+
			"}, {"+
			"  \"@id\" : \"urn:anno4j:a8c40ef0-bd61-4a99-9ff3-e098eb448f3d\","+
			"  \"@type\" : [ \"http://xmlns.com/foaf/0.1/Person\", \"https://github.com/anno4j/ns#Agent\" ],"+
			"  \"http://xmlns.com/foaf/0.1/name\" : [ {"+
			"    \"@value\" : \"A. Person\""+
			"  } ]"+
			"}, {"+
			"  \"@id\" : \"urn:anno4j:cf0dc208-cd16-4104-8b34-a53d2ed6b469\","+
			"  \"@type\" : [ \"http://www.w3.org/ns/prov/SoftwareAgent\", \"https://github.com/anno4j/ns#Agent\" ],"+
			"  \"http://xmlns.com/foaf/0.1/name\" : [ {"+
			"    \"@value\" : \"Code v2.1\""+
			"  } ]"+
			"} ]";
	
	public String getJsonLd() {
		return jsonLd;
	}

	public void setJsonLd(String jsonLd) {
		this.jsonLd = jsonLd;
	}

	public String getJsonLd2() {
		return jsonLd2;
	}

	public void setJsonLd2(String jsonLd2) {
		this.jsonLd2 = jsonLd2;
	}

}
