package edu.kit.masi.metastore.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arangodb.ArangoConfigure;
import com.arangodb.ArangoDriver;
import com.arangodb.ArangoException;
import com.arangodb.ArangoHost;
import com.arangodb.DocumentCursor;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.IndexesEntity;
import edu.kit.masi.metastore.exception.MetaStoreException;
import edu.kit.masi.metastore.exception.StatusCode;
import edu.kit.masi.metastore.utils.ArangoPropertyHandler;
import edu.kit.masi.metastore.model.MetsArangoPOJO;
import org.json.JSONException;

/**
 * Class handling all communication with database.
 * 
 * @author vaibhav
 */
public class ArangoDB {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArangoDB.class);

	private static final String TYPE_ATTRIBUTE = "type";
	private static final String XSD_ATTRIBUTE = "xsd";
	private static final String XML_ATTRIBUTE = "xml";
	private ArangoDriver driver;
	private String collectionName;

	public ArangoDB() {
		ArangoPropertyHandler propertyHdlr = new ArangoPropertyHandler();
		propertyHdlr.loadProperty();

		collectionName = propertyHdlr.getCollectionName();

		ArangoConfigure configure = new ArangoConfigure();
		ArangoHost arangoHost = new ArangoHost(propertyHdlr.getUrl(), propertyHdlr.getIntPort());
		configure.setArangoHost(arangoHost);

		configure.setDefaultDatabase(propertyHdlr.getDbName());
		configure.setUser(propertyHdlr.getUsername());
		configure.setPassword((null != propertyHdlr.getPassword() ? propertyHdlr.getPassword() : ""));
		configure.init();
		this.driver = new ArangoDriver(configure);
		if (propertyHdlr.getDbDelete()) {
			try {
				driver.deleteDatabase(propertyHdlr.getDbName());
			} catch (ArangoException e) {
			}
		}

		try {
			List<String> dblist = driver.getDatabases().getResult();
			if (!dblist.contains(propertyHdlr.getDbName())) {
				driver.createDatabase(propertyHdlr.getDbName());
			} else {
				// And what happens if not! At least log an error!
				LOGGER.error(
						"Collection " + collectionName + " not found during instantiating driver for arango database!");

			}
			HashMap allCollectionName = (HashMap) driver.getCollections().getNames();
			if (!allCollectionName.containsKey(collectionName)) {
				driver.createCollection(collectionName);
			} else {
				// And what happens if not! At least log an error!
				LOGGER.error(
						"Collection " + collectionName + " not found during instantiating driver for arango database!");
			}
		} catch (ArangoException e1) {
			LOGGER.error("Error while instantiating driver for arango database!", e1);
		}
	}

	public String postXSD(String hashedKey, String xsdString, String type) throws MetaStoreException {
		BaseDocument xmlForArango = new BaseDocument();
		xmlForArango.addAttribute(TYPE_ATTRIBUTE, type);
		xmlForArango.addAttribute(XSD_ATTRIBUTE, xsdString);
		xmlForArango.setDocumentKey(hashedKey);
		try {
			driver.createDocument(collectionName, xmlForArango);
		} catch (ArangoException e) {
			if (e.getMessage().equals("[1210] cannot create document, unique constraint violated")) {
				throw new MetaStoreException(e, StatusCode.CONFLICT.getStatusCode()); // conflict
			} else {
				throw new MetaStoreException(e);
			}
		}
		return "Successfully registered";
	}

	public String getRegisteredXsd(String hashValue) throws MetaStoreException {
		try {
			return driver.getDocument(collectionName + "/" + hashValue, BaseDocument.class).getEntity()
					.getAttribute(XSD_ATTRIBUTE).toString();
		} catch (ArangoException e) {
			throw new MetaStoreException("No XSD registered for hash '" + hashValue + "'", e);
		}
	}

	public String getRegisteredXsdType(String hashValue) throws MetaStoreException {
		try {
			return driver.getDocument(collectionName + "/" + hashValue, BaseDocument.class).getEntity()
					.getAttribute(TYPE_ATTRIBUTE).toString();
		} catch (ArangoException e) {
			throw new MetaStoreException("No type for XSD registered for hash '" + hashValue + "'", e);
		}
	}

	/**
	 * Write xml to Database
	 *
	 * @param hashedKey
	 *            hashed key of unique id
	 * @param xmlString
	 *            XML document
	 * @param type
	 *            type of the XML document (e.g.: prefix of namespace)
	 * @return Success otherwise an exception will be thrown.
	 * @throws MetaStoreException
	 *             Error during storage.
	 */
	public String postXML(String hashedKey, String xmlString, String type) throws MetaStoreException {
		BaseDocument xmlForArango = new BaseDocument();
		xmlForArango.addAttribute(TYPE_ATTRIBUTE, type);
		xmlForArango.addAttribute(XML_ATTRIBUTE, xmlString);
		xmlForArango.setDocumentKey(hashedKey);
		String returnMsg;
		try {
			driver.createDocument(collectionName, xmlForArango);
			returnMsg = "Successfully Registered.";
		} catch (ArangoException e) {
			if (e.getMessage().equals("[1210] cannot create document, unique constraint violated")) {
				throw new MetaStoreException(e, StatusCode.CONFLICT.getStatusCode()); // Conflict
			} else {
				throw new MetaStoreException(e);
			}
		}
		return returnMsg;
	}

	public void storeJSONRawDocument(String hashedKey, String finalString) throws MetaStoreException {
		try {
			driver.updateDocumentRaw(collectionName + "/" + hashedKey, finalString, null, false, null);
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}

	}

	public JSONObject getJsonObject(String hashedKey) throws MetaStoreException {
		try {

			String fetchData1 = driver.getDocumentRaw(collectionName + "/" + hashedKey, null, null);
			return new JSONObject(fetchData1).getJSONObject("json");

		} catch (ArangoException | JSONException e) {
			throw new MetaStoreException(e, StatusCode.NOT_FOUND.getStatusCode()); // resource
																					// not
																					// found
		}
	}

	public void applyIndexes(Set<String> indexSet) {
		for (String indSet : indexSet) {
			try {
				driver.createFulltextIndex(collectionName, indSet);
			} catch (ArangoException e) {
				LOGGER.error("Error while creating index for '" + indSet + "'!", e);
			}
		}
	}

	public String getCollectionName() {
		return collectionName;
	}

	public String storeXmlDocument(MetsArangoPOJO metsPOJO) throws MetaStoreException {
		DocumentEntity<MetsArangoPOJO> entity = null;
		try {
			entity = driver.createDocument(collectionName, metsPOJO);
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}
		return entity.getDocumentKey();
	}

	public IndexesEntity getIndexes() throws MetaStoreException {
		try {
			return driver.getIndexes(collectionName);
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}
	}

	public DocumentCursor<ArangoDriver> applyFullTextSearch(String indexEntity, String text) throws MetaStoreException {
		try {
			return driver.executeSimpleFulltextWithDocuments(collectionName, indexEntity, "prefix:" + text, 0, 0, null,
					null);
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}
	}

	public String getXMLData(String hashedValue) throws MetaStoreException {
		try {
			return driver.getDocument(collectionName + "/" + hashedValue, BaseDocument.class).getEntity()
					.getAttribute(XML_ATTRIBUTE).toString();
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}
	}

	public DocumentCursor getAllChildDocuments(String hashedValue) throws MetaStoreException {
		String allIDDocQuery = "FOR doc IN " + collectionName
				+ " filter doc.mainXmlHandler==@mainXmlHandler && doc.xmlData != null return doc";
		Map<String, Object> bindingVals = new HashMap<>();
		bindingVals.put("mainXmlHandler", collectionName + "/" + hashedValue);
		try {
			return driver.executeDocumentQuery(allIDDocQuery, bindingVals, null, BaseDocument.class);
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}
	}

	public DocumentCursor getContentMetaDataXml(String uniqueID, String nameSpace) throws MetaStoreException {

		String allIDDocQuery = "FOR doc IN " + collectionName
				+ " filter doc.mainXmlHandler==@mainXmlHandler && doc.type==@type return doc";
		Map<String, Object> bindingVals = new HashMap<>();
		bindingVals.put("mainXmlHandler", uniqueID);
		bindingVals.put("type", nameSpace);
		try {
			return driver.executeDocumentQuery(allIDDocQuery, bindingVals, null, BaseDocument.class);
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}
	}

	/*
	 * public String updateXMLData(String documentHandle, String xmlData, String
	 * finalStr, Map<String, String> arrayData) throws MetaStoreException {
	 * String updateQuery = "FOR doc IN " + collectionName +
	 * " FILTER doc._id==@mainXmlHandler UPDATE doc WITH { xmlData: @xmlData ,json :@json} IN "
	 * + collectionName; Map<String, Object> bindingVals = new HashMap<>();
	 * bindingVals.put("mainXmlHandler", documentHandle);
	 * bindingVals.put("xmlData", xmlData); bindingVals.put("json", ""); try {
	 * driver.executeDocumentQuery(updateQuery, bindingVals, null,
	 * BaseDocument.class); String updateArray = "for doc in " + collectionName
	 * +
	 * " FILTER doc._id==@mainXmlHandler UPDATE doc WITH {oldXMLData: PUSH(doc.oldXMLData, @arrayData)} IN "
	 * + collectionName; Map<String, Object> bindingVals2 = new HashMap<>();
	 * bindingVals2.put("mainXmlHandler", documentHandle);
	 * bindingVals2.put("arrayData", arrayData);
	 * 
	 * driver.updateDocumentRaw(documentHandle, finalStr, null, false, null);
	 * driver.executeAqlQuery(updateArray, bindingVals2, null,
	 * BaseDocument.class);
	 * 
	 * } catch (ArangoException e) { throw new
	 * MetaStoreException("Error while storing data!", e); } return
	 * "Successfully stored the json and XmlData"; }
	 */

	public String getDigitalObjectIdForDocument(String hashedKey) throws MetaStoreException {
		String digitalObjectId;
		try {

			String fetchData1 = driver.getDocumentRaw(collectionName + "/" + hashedKey, null, null);
			digitalObjectId = new JSONObject(fetchData1).getString("mainXmlHandler").toString();
		} catch (Exception e) {
			LOGGER.error("Excepiton :" + e.getMessage());
			throw new MetaStoreException(e);
		}
		return digitalObjectId;
	}

	public DocumentCursor<BaseDocument> getSectionDocumentKey(String nameSpace, String pDigitalObjectId,
			String pSectionId) throws MetaStoreException {
		StringBuilder allIDDocQuery = new StringBuilder();
		allIDDocQuery.append(
				"FOR doc IN " + collectionName + " filter doc.mainXmlHandler==@mainXmlHandler && doc.type==@type ");
		Map<String, Object> bindingVals = new HashMap<>();
		bindingVals.put("mainXmlHandler", pDigitalObjectId);
		bindingVals.put("type", nameSpace);
		if (!pSectionId.equals("")) {
			allIDDocQuery.append("&& doc.id==@id");
			bindingVals.put("id", pSectionId);
		}
		allIDDocQuery.append(" return doc");

		try {
			return driver.executeDocumentQuery(allIDDocQuery.toString(), bindingVals, null, BaseDocument.class);
		} catch (ArangoException e) {
			throw new MetaStoreException(e);
		}
	}

	/**
	 * This Method is used to update the partial section of the complete mets
	 * file. This method does update process by performing following steps 
	 * 1.stores older xml data into array 
	 * 2. delete exsiting Json 
	 * 3. store new xml data 
	 * 4. store new json data 5. apply indexing to new json data
	 * 
	 * @param documentToUpdateKey
	 * @param pSectionDocument
	 * @param finalStr
	 * @param arrayData
	 * @return
	 * @throws MetaStoreException 
	 */
	public String updateXMLData(String documentToUpdateKey, String pSectionDocument, String finalStr,
			Map<String, String> arrayData) throws MetaStoreException {

		String updateQuery = "FOR doc IN " + collectionName
				+ " FILTER doc._id==@mainXmlHandler UPDATE doc WITH { xmlData: @xmlData ,json :@json} IN "
				+ collectionName;
		Map bindingVals = new HashMap<String, Object>();
		bindingVals.put("mainXmlHandler", collectionName+"/"+documentToUpdateKey);
		bindingVals.put("xmlData", pSectionDocument);
		bindingVals.put("json", "");
		try {
			driver.executeDocumentQuery(updateQuery, bindingVals, null, BaseDocument.class);
		} catch (ArangoException e) {
			 throw new MetaStoreException(e.getMessage(),StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
			String updateArray = "for doc in " + collectionName	+ " FILTER doc._id==@mainXmlHandler UPDATE doc WITH {oldXMLData: PUSH(doc.oldXMLData, @arrayData)} IN "
					+ collectionName;
			Map bindingVals2 = new HashMap<String, Object>();
			bindingVals2.put("mainXmlHandler", collectionName+"/"+documentToUpdateKey);
			bindingVals2.put("arrayData", arrayData);
			try {
				driver.updateDocumentRaw(collectionName+"/"+documentToUpdateKey, finalStr, null, false, null);
				driver.executeAqlQuery(updateArray, bindingVals2, null, BaseDocument.class);
			} catch (ArangoException e) {
				throw new MetaStoreException(e.getMessage(),StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
			}
			return "Successfully stored the json and XmlData";

	}

}
