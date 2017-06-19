package edu.kit.masi.metastore.utils;

import edu.kit.masi.metastore.db.ArangoDB;
import edu.kit.masi.metastore.exception.MetaStoreException;
import edu.kit.masi.metastore.model.MetsArangoPOJO;
import edu.kit.masi.metastore.model.SectionDocument;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaStoreUtility {

  /**
   * Logger.
   */
  public static final Logger LOGGER = LoggerFactory.getLogger(MetaStoreUtility.class);

  /**
   * Transform string to byte array.
   *
   * @param targetNameSpace Any string.
   * @return Representation of string as byteArray.
   */
  public static String getHashValue(String targetNameSpace) {
    StringBuilder sb = new StringBuilder();
    for (byte b : targetNameSpace.getBytes(Charset.forName("UTF-8"))) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return sb.toString();
  }

  public static Set<String> applyIndexing(JSONObject jsonObj) {
    Set<String> indexSet = new HashSet<>();
    for (Object keys : jsonObj.keySet()) {
      String nKeys = (String) keys;
      Object keyvalue = jsonObj.get(nKeys);

      if (keyvalue instanceof JSONObject) {
        getRecursiveKeys(keyvalue, nKeys, indexSet);
      }
    }
    return indexSet;
  }

  private static String getRecursiveKeys(Object keys, String nKeys, Set<String> indexSet) {
    JSONObject jsonobj = (JSONObject) keys;

    for (Object keysets : jsonobj.keySet()) {
      StringBuilder pathKey = new StringBuilder();
      String nkeys = (String) keysets;

      pathKey.append(nKeys).append(".").append(nkeys);

      Object keyvalue = jsonobj.get(nkeys);

      if (keyvalue instanceof JSONArray) {

        JSONArray keyValueArray = (JSONArray) keyvalue;
        for (int i = 0; i < keyValueArray.length(); i++) {
          try {
            JSONObject arrayToObj = keyValueArray.getJSONObject(i);
            pathKey.append(".").append(getRecursiveKeys(arrayToObj, pathKey.toString(), indexSet));
          } catch (JSONException e) {
          }
        }
      }

      if (keyvalue instanceof JSONObject) {
        pathKey.append(".").append(getRecursiveKeys(keyvalue, pathKey.toString(), indexSet));
      }

      if (!pathKey.toString().endsWith(".")) {
        indexSet.add("json." + pathKey.toString().replaceAll("\\.\\.+", "."));
      }
    }
    return "";
  }

  /**
   * Write nested section document to arango.
   *
   * @param pArango Instance for communicating with database.
   * @param pSectionDoc Instance holding all information about nested section.
   * @throws MetaStoreException An error occurred.
   */
  public static void storeNestedSection(ArangoDB pArango, SectionDocument pSectionDoc) throws MetaStoreException {
    try {
    String xmlDocument = XmlUtility.xmlToString(pSectionDoc.getRootNode());
    // Two step storing document into arangodb 
    // 1.Step is to store in model as we need to store the XML content into AranogDB and it cannot be stored with rawDocument in ArangoDB
    MetsArangoPOJO metsPojo = new MetsArangoPOJO();
    metsPojo.setId(pSectionDoc.getSectionId());
    metsPojo.setMainXmlHandler(pSectionDoc.getDigitalObjectId());
    metsPojo.setXmlData(xmlDocument);
    String arangoDocumentHandler = pArango.storeXmlDocument(metsPojo);
  // 2. Step is to update the above document in raw document format as arango allows in this way to store JSON data.
    JSONObject str2json = XML.toJSONObject(xmlDocument);
    String finalStr = "{\"type\":\"" + pSectionDoc.getType() + "\",\"json\":" + str2json
            + ",\"mainXmlHandler\":\"" + pSectionDoc.getDigitalObjectId() + "\"}";
    pArango.storeJSONRawDocument(arangoDocumentHandler, finalStr);

    // Applying Indexing operations. 
    // Index are applied on the stored json as aranogdb adds MAP attribute to some arrays, So to avoid unknow indexing.
    JSONObject getStoredJson = pArango.getJsonObject(arangoDocumentHandler);
    Set<String> indexSet = applyIndexing(getStoredJson);
    pArango.applyIndexes(indexSet);
    } catch (JSONException jex) {
      throw new MetaStoreException("Error creating JSON document for section id: " + pSectionDoc.getSectionId(), jex);
    }
  }
}
