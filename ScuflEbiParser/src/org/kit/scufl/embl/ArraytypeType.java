//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:01:34 PM CET 
//


package org.kit.scufl.embl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for arraytypeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="arraytypeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}elementtype"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}xmlElementAttributes"/>
 *       &lt;attribute name="qname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "arraytypeType", propOrder = {
    "elementtype"
})
public class ArraytypeType {

    @XmlElement(required = true)
    protected ElementtypeType elementtype;
    @XmlAttribute(name = "qname")
    protected String qname;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "optional")
    protected Boolean optional;
    @XmlAttribute(name = "unbounded")
    protected Boolean unbounded;
    @XmlAttribute(name = "wrapped")
    protected Boolean wrapped;
    @XmlAttribute(name = "typename")
    protected String typename;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the elementtype property.
     * 
     * @return
     *     possible object is
     *     {@link ElementtypeType }
     *     
     */
    public ElementtypeType getElementtype() {
        return elementtype;
    }

    /**
     * Sets the value of the elementtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementtypeType }
     *     
     */
    public void setElementtype(ElementtypeType value) {
        this.elementtype = value;
    }

    /**
     * Gets the value of the qname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQname() {
        return qname;
    }

    /**
     * Sets the value of the qname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQname(String value) {
        this.qname = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the optional property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOptional() {
        return optional;
    }

    /**
     * Sets the value of the optional property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOptional(Boolean value) {
        this.optional = value;
    }

    /**
     * Gets the value of the unbounded property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUnbounded() {
        return unbounded;
    }

    /**
     * Sets the value of the unbounded property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUnbounded(Boolean value) {
        this.unbounded = value;
    }

    /**
     * Gets the value of the wrapped property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWrapped() {
        return wrapped;
    }

    /**
     * Sets the value of the wrapped property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWrapped(Boolean value) {
        this.wrapped = value;
    }

    /**
     * Gets the value of the typename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypename() {
        return typename;
    }

    /**
     * Sets the value of the typename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypename(String value) {
        this.typename = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
