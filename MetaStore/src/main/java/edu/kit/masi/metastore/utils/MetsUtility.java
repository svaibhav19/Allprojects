package edu.kit.masi.metastore.utils;

import edu.kit.masi.metastore.model.MetsArangoPOJO;
import edu.kit.masi.metastore.model.SectionDocument;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.arangodb.ArangoDriver;
import com.arangodb.DocumentCursor;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.IndexEntity;
import com.arangodb.entity.IndexesEntity;
import edu.kit.masi.metastore.db.ArangoDB;
import edu.kit.masi.metastore.exception.MetaStoreException;
import edu.kit.masi.metastore.exception.StatusCode;
import edu.kit.masi.metastore.model.ReturnType;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;

/**
 * Utilities for handling METS files.
 *
 * @author vaibhav/hartmann-v
 */
public class MetsUtility {

	public static final String METS_NAMESPACE = "http://www.loc.gov/METS/";

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MetsUtility.class);
	private static final String METS_PREFIX = "mets:";
	private final ArangoDB arango;

	public MetsUtility(ArangoDB pArango) {
		arango = pArango;
	}

	/**
	 * Segregate/Divide xml to different documents.
	 *
	 * @param incomingXML
	 *            xml containing whole METS file
	 * @param documentHandler
	 * @throws MetaStoreException
	 */
	public void segregateNStoreJson(String incomingXML, String documentHandler) throws MetaStoreException {

		try {

			InputSource source = new InputSource(new StringReader(incomingXML));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(source);
			doc.getDocumentElement().normalize();

			// XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//*[local-name()='mets']/*";
			// this the expression to break the xml in different groups
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); i++) {

				MetsArangoPOJO metsPOJO = new MetsArangoPOJO();

				Node node = nodeList.item(i);
				if (node.getNodeName().replaceAll(METS_PREFIX, "").equalsIgnoreCase("metsHdr")
						|| node.getNodeName().replaceAll(METS_PREFIX, "").equalsIgnoreCase("fileSec")
						|| node.getNodeName().replaceAll(METS_PREFIX, "").equalsIgnoreCase("structMap")) {

				} else {

					StringWriter writer1 = new StringWriter();
					Transformer transformer = TransformerFactory.newInstance().newTransformer();

					transformer.transform(new DOMSource(node), new StreamResult(writer1));

					JSONObject str2json = XML.toJSONObject(writer1.toString());
					metsPOJO.setXmlData(writer1.toString());
					metsPOJO.setMainXmlHandler(documentHandler);
					metsPOJO.setSections(node.getNodeName().replaceAll(METS_PREFIX, ""));
					metsPOJO.setId(node.getAttributes().getNamedItem("ID").getNodeValue().toString());

					String xmlKey = arango.storeXmlDocument(metsPOJO);
					String finalStr = "{\"type\":\"" + node.getNodeName() + "\",\"json\":" + str2json
							+ ",\"mainXmlHandler\":\"" + documentHandler + "\"}";
					arango.storeJSONRawDocument(xmlKey, finalStr);

					// Applying Indexing operations
					JSONObject getStoredJson = arango.getJsonObject(xmlKey);
					Set<String> indexSet = MetaStoreUtility.applyIndexing(getStoredJson);
					arango.applyIndexes(indexSet);
				}
			}

		} catch (IOException | ParserConfigurationException | TransformerException | XPathExpressionException
				| JSONException | DOMException | SAXException e) {
			throw new MetaStoreException(e);
		}

	}

	/**
	 * Get all digital object ids of mets documents with matching search term.
	 *
	 * @param searchTerm
	 *            search term
	 * @return Collection with all matching digital object ids.
	 * @throws MetaStoreException
	 *             an error occurred.
	 */
	public Set<String> searchFullTextWithId(String searchTerm) throws MetaStoreException {

		Set<String> digitalObjID = new HashSet<>();

		for (IndexEntity indexEntity : arango.getIndexes().getIndexes()) {
			if (indexEntity.getType().toString().equalsIgnoreCase("fulltext")) {
				// below method is used to get all the
				DocumentCursor<ArangoDriver> cursuroResults = arango.applyFullTextSearch(indexEntity.getFields().get(0),
						searchTerm);
				for (DocumentEntity<ArangoDriver> documentEntity : cursuroResults) {
					digitalObjID.add(arango.getDigitalObjectIdForDocument(documentEntity.getDocumentKey()));
				}
			}
		}
		return digitalObjID;
	}

	/*
	 * public String searchFullText(String text) throws MetaStoreException {
	 * 
	 * IndexesEntity indexdes = null; List<IndexEntity> indexList = null;
	 * Set<String> uniqueHanderls = new HashSet<>(); StringBuilder finalJson =
	 * new StringBuilder();
	 * 
	 * indexdes = arango.getIndexes(); indexList = indexdes.getIndexes();
	 * 
	 * for (IndexEntity indexEntity : indexList) { if
	 * (indexEntity.getType().toString().equalsIgnoreCase("fulltext")) {
	 * DocumentCursor<ArangoDriver> cursuroResults = null; cursuroResults =
	 * arango.applyFullTextSearch(indexEntity.getFields().get(0), text);
	 * 
	 * for (DocumentEntity<ArangoDriver> documentEntity : cursuroResults) {
	 * uniqueHanderls.add(documentEntity.getDocumentKey().toString()); } } } for
	 * (String documentKey : uniqueHanderls) {
	 * finalJson.append(arango.getJsonObject(documentKey)); } return
	 * finalJson.toString();
	 * 
	 * }
	 */

	/**
	 * Download METS with given unique ID as JSON or XML
	 *
	 * @param hashedValue
	 *            unique ID.
	 * @return XML document as string.
	 * @throws MetaStoreException
	 *             An error occurred.
	 */
	public String downloadFullXML(String hashedValue, ReturnType returnType) throws MetaStoreException {
		String xmlString = arango.getXMLData(hashedValue);

		Document doc2 = XmlUtility.strToXmlDocument(xmlString);
		DocumentCursor cursor = arango.getAllChildDocuments(hashedValue);
		Iterator iterator = cursor.entityIterator();

		XPath xPath = XPathFactory.newInstance().newXPath();
		while (iterator.hasNext()) {
			BaseDocument aDocument = (BaseDocument) iterator.next();

			String xmpathExp = "//*[local-name()='xmlData'][../../@ID='" + aDocument.getAttribute("id") + "']";
			try {
				NodeList nodeList = (NodeList) xPath.compile(xmpathExp).evaluate(doc2, XPathConstants.NODESET);
				if (nodeList.getLength() > 0) {
					Node xmlDataNode = nodeList.item(0); // xmlData node
					Node importedNode = XmlUtility.strToXmlDocument(aDocument.getAttribute("xmlData").toString())
							.getFirstChild();
					xmlDataNode.removeChild(xmlDataNode.getFirstChild());
					xmlDataNode.appendChild(doc2.importNode(importedNode, true));
				}
			} catch (XPathExpressionException | DOMException e) {
				throw new MetaStoreException(e);
			}
		}
		// ***************************************************************************
		// The next 4 lines are no longer needed due to store xmlData childs
		// directly.
		// String filesecpathExp = "//*[local-name()='fileSec']";
		// String stuctpathExp = "//*[local-name()='structMap']";
		// changeStructure(filesecpathExp, doc2, xPath);
		// changeStructure(stuctpathExp, doc2, xPath);
		// ***************************************************************************
		String metsString = XmlUtility.xmlToString(doc2);
		if (returnType == ReturnType.JSON) {
			JSONObject toJSONObject = XML.toJSONObject(metsString);
			metsString = toJSONObject.toString();
		}
		return metsString;
	}

	/**
	 * Validate all section documents (xmlData) of a mets file.
	 *
	 * @param pMetsXml
	 *            mets file as string.
	 * @param pDigitalObjectId
	 *            uniqueId of the mets file.
	 * @return List containing all nested sections.
	 */
	public List<SectionDocument> validateNestedSections(String pMetsXml, String pDigitalObjectId)
			throws MetaStoreException {
		List<SectionDocument> allSections = getAllSections(pMetsXml, pDigitalObjectId);
		// Validate all nested sections
		for (SectionDocument sectionDoc : allSections) {
			if (!validateSectionDocument(sectionDoc)) {
				throw new MetaStoreException("Invalid section '" + sectionDoc.getSectionId() + "'!",
						StatusCode.BAD_REQUEST.getStatusCode());
			}
		}
		return allSections;
	}

	/**
	 * Get all section documents (xmlData) of a mets file.
	 *
	 * @param pMetsXml
	 *            mets file as string.
	 * @param pDigitalObjectId
	 *            uniqueId of the mets file.
	 * @return List containing all documents.
	 */
	private List<SectionDocument> getAllSections(String pMetsXml, String pDigitalObjectId) throws MetaStoreException {
		return getAllSections(XmlUtility.strToXmlDocument(pMetsXml), pDigitalObjectId);
	}

	/**
	 * Get all section documents (xmlData) of a mets file.
	 *
	 * @param pMetsDocument
	 *            mets file as string.
	 * @param pDigitalObjectId
	 *            uniqueId of the mets file.
	 * @return List containing all documents.
	 */
	private List<SectionDocument> getAllSections(Document pMetsDocument, String pDigitalObjectId)
			throws MetaStoreException {
		List<SectionDocument> allSections = new ArrayList<>();

		XPath xPath = XPathFactory.newInstance().newXPath();
		String xmpathExp = "//*[local-name()='xmlData']/*";
		try {
			NodeList nodeList = (NodeList) xPath.compile(xmpathExp).evaluate(pMetsDocument, XPathConstants.NODESET);
			for (int index = 0; index < nodeList.getLength(); index++) {
				SectionDocument sd;
				// Determining ID of the section.
				Node sectionNode = nodeList.item(index).getParentNode().getParentNode().getParentNode();
				String sectionId = sectionNode.getAttributes().getNamedItem("ID").getNodeValue();
				Node rootNode = nodeList.item(index);
				sd = new SectionDocument(pDigitalObjectId, sectionId, rootNode);
				allSections.add(sd);
			}
		} catch (XPathExpressionException | DOMException e) {
			throw new MetaStoreException(e);
		}
		return allSections;
	}

	private void changeStructure(String filesecpathExp, Document doc2, XPath xPath) {
		NodeList nodeList = null;
		try {
			nodeList = (NodeList) xPath.compile(filesecpathExp).evaluate(doc2, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node parent = nodeList.item(i).getParentNode();
			parent.removeChild(nodeList.item(i));
			parent.appendChild(nodeList.item(i));
		}

	}

	// public String getContentMetaData(String uniqueID, String section, String
	// id, String addSection, String id2)
	// throws MetaStoreException {
	//
	//// DocumentCursor cursor = arango.getContentMetaDataXml(uniqueID, section,
	// id);
	// if (null != cursor) {
	// XPath xPath = XPathFactory.newInstance().newXPath();
	// Iterator iterator = cursor.entityIterator();
	// if (iterator.hasNext()) {
	// BaseDocument aDocument = (BaseDocument) iterator.next();
	// StringBuilder xpathBuilder = new StringBuilder();
	// if (!addSection.equals("")) {
	// xpathBuilder.append("//*[local-name()='" + addSection + "']");
	//
	// if (!id2.equals("")) {
	// xpathBuilder.append("[@ID='" + id2 + "']");
	// }
	//
	// xpathBuilder.append("/*/*[local-name()='xmlData']");
	// } else {
	// xpathBuilder.append("//*[local-name()='xmlData']");
	// }
	// Document doc2 =
	// XmlUtility.strToXmlDocument(aDocument.getAttribute("xmlData").toString());
	// // XPath xPath = XPathFactory.newInstance().newXPath();
	// NodeList nodeList;
	// try {
	// nodeList = (NodeList)
	// xPath.compile(xpathBuilder.toString()).evaluate(doc2,
	// XPathConstants.NODESET);
	// if (null != nodeList) {
	// if (nodeList.getLength() > 1) {
	// return "Error: Multiple Content Data Found Please Provide additonal
	// parameters";
	// } else {
	// Node node = nodeList.item(0);
	// return XmlUtility.xmlToString(node);
	// }
	// } else {
	// return "Error: No Content Data Found Please Try Again";
	// }
	//
	// } catch (XPathExpressionException e) {
	// e.printStackTrace();
	// }
	//
	// return "Error : Fetching Record";
	// } else {
	// return "Error : Record not found";
	// }
	// } else {
	// return "Error : Invalid Search Parameteres. No Document Found For
	// Specified Unique ID";
	// }
	//
	// }

	// public String validateAndUpdate(String fullXml, Node newNode, String
	// uniqueID, String section, String id,
	// String addSection, String id2, String targetNamespace) throws
	// MetaStoreException {
	//
	// Document doc2 = XmlUtility.strToXmlDocument(fullXml);
	// XPath xPath = XPathFactory.newInstance().newXPath();
	// StringBuilder xpathBuilder = new StringBuilder();
	// xpathBuilder.append("//*[local-name()='" + section + "'][@ID='" + id +
	// "']");
	// if (!addSection.equals("")) {
	// xpathBuilder.append("/*[local-name()='" + addSection + "']");
	// if (!id2.equals("")) {
	// xpathBuilder.append("[@ID='" + id2 + "']");
	// }
	// xpathBuilder.append("/*/*[local-name()='xmlData']");
	// } else {
	// xpathBuilder.append("/*/*[local-name()='xmlData']");
	// }
	//
	// try {
	// NodeList nodeList = (NodeList)
	// xPath.compile(xpathBuilder.toString()).evaluate(doc2,
	// XPathConstants.NODESET);
	// if (null != nodeList || nodeList.getLength() != 0) {
	// if (nodeList.getLength() > 1) {
	// throw new MetaStoreException("Multiple Path Found Please Provide
	// Additional Parameters");
	// }
	// Node parent = nodeList.item(0).getParentNode();
	// parent.removeChild(nodeList.item(0));
	// parent.appendChild(doc2.importNode(newNode.getFirstChild(), true));
	// String amdSecPathExp = "//*[local-name()='amdSec']";
	// String filesecpathExp = "//*[local-name()='fileSec']";
	// String stuctpathExp = "//*[local-name()='structMap']";
	// changeStructure(amdSecPathExp, doc2, xPath);
	// changeStructure(filesecpathExp, doc2, xPath);
	// changeStructure(stuctpathExp, doc2, xPath);
	//
	// } else {
	// throw new MetaStoreException(
	// "Invalid parameters: could not locate the path. Please check the
	// parameters!");
	// }
	//
	// } catch (DOMException | XPathExpressionException e) {
	// throw new MetaStoreException(e);
	// }
	//
	// if (XmlUtility.validate(XmlUtility.xmlToString(doc2),
	// arango.getRegisteredXsd(MetaStoreUtility.getHashValue(targetNamespace))))
	// {
	// // code for insertions update
	// try {
	// NodeList nodeList = (NodeList) xPath.compile("//*[local-name()='" +
	// section + "'][@ID='" + id + "']")
	// .evaluate(doc2, XPathConstants.NODESET);
	// if (nodeList.getLength() > 0) {
	// Node node = nodeList.item(0);
	// DocumentCursor currentCursor =
	// arango.getContentMetaDataXml(MetaStoreUtility.getHashValue(uniqueID),
	// section, id);
	// Iterator iterator = currentCursor.entityIterator();
	// BaseDocument entity = (BaseDocument) iterator.next();
	// String oldXML = (String) entity.getAttribute("xmlData");
	//
	// Date toDaysDate = new Date();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
	// System.out.println(oldXML);
	// Map<String, String> arrayData = new HashMap<>();
	// arrayData.put("xmlData", oldXML);
	// arrayData.put("modifiedDate", sdf.format(toDaysDate));
	//
	// String finalStr = "{\"json\":" +
	// XML.toJSONObject(XmlUtility.xmlToString(node)) + "}";
	// String xmlKey = arango.updateXMLData(entity.getDocumentHandle(),
	// XmlUtility.xmlToString(node),
	// finalStr, arrayData);
	// }
	//
	// } catch (XPathExpressionException | JSONException e) {
	// throw new MetaStoreException(e);
	// }
	// } else {
	// throw new MetaStoreException("Invalid xml data please try again");
	// }
	// return "Successfully updated the xml file";
	// }

	// <editor-fold defaultstate="collapsed" desc="write xml document">
	/**
	 * Validate and store/update (if valid) METS document to metastore.
	 * 
	 * @param pMetsDocument
	 *            METS document
	 * @param pUniqueId
	 *            Unique ID
	 * @throws MetaStoreException
	 *             An error occurred.
	 */
	public void validateAndStoreMets(Document pMetsDocument, String pUniqueId) throws MetaStoreException {
		validateMets(pMetsDocument);

		writeMets(pMetsDocument, pUniqueId);
	}

	/**
	 * Store/update (if valid) METS document to metastore. This method is not
	 * public available as the validation step has to be done in beforehand.
	 * 
	 * @param pMetsDocument
	 *            METS document
	 * @param pUniqueId
	 *            Unique ID
	 * @throws MetaStoreException
	 *             An error occurred.
	 */
	private void writeMets(Document pMetsDocument, String pUniqueId) throws MetaStoreException {
		// Store METS document
		//
		// Determine sections
		// Store sections
	}

	private void storeXML(Node pRootNode, String pUniqueId) throws MetaStoreException {

	}

	/**
	 * Store a section to metastore
	 * 
	 * @param pSection
	 * @param pUniqueId
	 */
	private void storeSection(SectionDocument pSection, String pUniqueId) {

	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Validation of document">
	/**
	 * Validate METS document.
	 *
	 * @param pMetsDocument
	 *            Mets document
	 * @return valid or not
	 * @throws MetaStoreException
	 *             An error occurred.
	 */
	public boolean validateMets(Document pMetsDocument) throws MetaStoreException {
		boolean isValid = false;
		// Validate Mets document
		validateNode(pMetsDocument.getDocumentElement());
		// split in multiple sections and validate them separately
		// for all sections: validateNode(sectionNode);

		// Everything works fine. Set isValid to true;
		isValid = true;
		return isValid;
	}

	/**
	 * Validate section document.
	 *
	 * @param pSection
	 *            Section document.
	 * @return valid or not.
	 * @throws MetaStoreException
	 *             An error occurred.
	 */
	public boolean validateSectionDocument(SectionDocument pSection) throws MetaStoreException {
		String hashValue = MetaStoreUtility.getHashValue(pSection.getType());

		return validateNode(pSection.getRootNode(), hashValue);
	}

	/**
	 * Validate xml document.
	 *
	 * @param pRootNode
	 *            Root node of the document.
	 * @param hashOfXsd
	 *            Hash of the xsd file.
	 * @return valid or not.
	 * @throws MetaStoreException
	 *             An error occurred.
	 */
	public boolean validateNode(Node pRootNode) throws MetaStoreException {
		String hashValue = MetaStoreUtility.getHashValue(pRootNode.getNamespaceURI());

		return validateNode(pRootNode, hashValue);

	}

	/**
	 * Validate xml document.
	 *
	 * @param pRootNode
	 *            Root node of the document.
	 * @param pHashOfXsd
	 *            Hash of the xsd file.
	 * @return valid or not.
	 * @throws MetaStoreException
	 *             An error occurred.
	 */
	public boolean validateNode(Node pRootNode, String pHashOfXsd) throws MetaStoreException {
		String xsdString = arango.getRegisteredXsd(pHashOfXsd);
		String xmlString = XmlUtility.xmlToString(pRootNode);
		return XmlUtility.validate(xmlString, xsdString);
		// return validateAgainstXsd(xsdString, xmlString);
	}
	// </editor-fold>

	/**
	 * This Method checks for all the Documents in ArangoDb for type=namespace and digitalObject ID.
	 * All the matched documents are return as string either in JSON/XML format.
	 * @param pType nameSpace
	 * @param pDigitalObjectId digitalObjectId
	 * @param returnType String (JSON/XML)
	 * @return
	 * @throws MetaStoreException
	 */
	public String getMetsSectionByNameSpace(String pType, String pDigitalObjectId, ReturnType returnType)
			throws MetaStoreException {
		DocumentCursor<BaseDocument> allSections1 = arango.getContentMetaDataXml(pDigitalObjectId, pType);
		StringBuilder allSection = new StringBuilder();
		
		if (returnType.equals(ReturnType.XML)) {
//			this block creates array of xml data 
			allSection.append("<array>\n");
			for (DocumentEntity<BaseDocument> documentEntity : allSections1) {
				allSection.append(documentEntity.getEntity().getAttribute("xmlData").toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")+"\n");
			}
			allSection.append("</array>");
		} else if (returnType.equals(ReturnType.JSON)) {
//			this block creates json array 
			JSONArray allSectionJson = new JSONArray();
			for (DocumentEntity<BaseDocument> documentEntity : allSections1) {
				allSectionJson.put(documentEntity.getEntity().getAttribute("json"));
			}
			allSection.append(allSectionJson.toString());
		}
		return allSection.toString();
	}
}
