//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:00:21 PM CET 
//


package org.kit.scufl.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScriptInputUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScriptInputUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="file" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tempFile" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="binary" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="charsetName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="forceCopy" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="list" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="concatenate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="mime" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScriptInputUser", namespace = "", propOrder = {
    "tag",
    "file",
    "tempFile",
    "binary",
    "charsetName",
    "forceCopy",
    "list",
    "concatenate",
    "mime"
})
public class ScriptInputUser {

    @XmlElement(required = true)
    protected String tag;
    protected boolean file;
    protected boolean tempFile;
    protected boolean binary;
    @XmlElement(required = true)
    protected String charsetName;
    protected boolean forceCopy;
    protected boolean list;
    protected boolean concatenate;
    @XmlElement(required = true)
    protected Object mime;

    /**
     * Gets the value of the tag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the value of the tag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTag(String value) {
        this.tag = value;
    }

    /**
     * Gets the value of the file property.
     * 
     */
    public boolean isFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     * 
     */
    public void setFile(boolean value) {
        this.file = value;
    }

    /**
     * Gets the value of the tempFile property.
     * 
     */
    public boolean isTempFile() {
        return tempFile;
    }

    /**
     * Sets the value of the tempFile property.
     * 
     */
    public void setTempFile(boolean value) {
        this.tempFile = value;
    }

    /**
     * Gets the value of the binary property.
     * 
     */
    public boolean isBinary() {
        return binary;
    }

    /**
     * Sets the value of the binary property.
     * 
     */
    public void setBinary(boolean value) {
        this.binary = value;
    }

    /**
     * Gets the value of the charsetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * Sets the value of the charsetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCharsetName(String value) {
        this.charsetName = value;
    }

    /**
     * Gets the value of the forceCopy property.
     * 
     */
    public boolean isForceCopy() {
        return forceCopy;
    }

    /**
     * Sets the value of the forceCopy property.
     * 
     */
    public void setForceCopy(boolean value) {
        this.forceCopy = value;
    }

    /**
     * Gets the value of the list property.
     * 
     */
    public boolean isList() {
        return list;
    }

    /**
     * Sets the value of the list property.
     * 
     */
    public void setList(boolean value) {
        this.list = value;
    }

    /**
     * Gets the value of the concatenate property.
     * 
     */
    public boolean isConcatenate() {
        return concatenate;
    }

    /**
     * Sets the value of the concatenate property.
     * 
     */
    public void setConcatenate(boolean value) {
        this.concatenate = value;
    }

    /**
     * Gets the value of the mime property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getMime() {
        return mime;
    }

    /**
     * Sets the value of the mime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setMime(Object value) {
        this.mime = value;
    }

}
