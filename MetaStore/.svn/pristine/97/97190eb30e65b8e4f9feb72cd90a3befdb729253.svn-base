package edu.kit.masi.metastore.utils;

import java.util.Properties;

public class ArangoPropertyHandler {

	private String collectionName;
	private String dbName;
	private String username;
	private String password;
	private String url;
	private String port;
	private boolean dbDelete;

	public String getPort() {
		return port;
	}

	public int getIntPort() {
		return Integer.parseInt(port);
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getDbDelete() {
		return dbDelete;
	}

	public void setDbDelete(boolean dbDelete) {
		this.dbDelete = dbDelete;
	}

	public void loadProperty() {
		try {
			Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("/DatabaseProperties.properties"));
			setCollectionName(prop.getProperty("collections"));
			setDbName(prop.getProperty("arangoDbName"));
			setUsername(prop.getProperty("username"));
			setPassword(prop.getProperty("password"));
			setUrl(prop.getProperty("arangoIP"));
			setPort(prop.getProperty("arangoPort"));
			setDbDelete(prop.getProperty("dbDelete").equalsIgnoreCase("true")?true:false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
