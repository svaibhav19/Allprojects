//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.09 at 03:17:22 PM CET 
//


package org.kit.xscufl.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for actionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="target" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="statechange" type="{http://org.embl.ebi.escience/xscufl/0.1alpha}statechangeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actionType", propOrder = {
    "target",
    "statechange"
})
public class ActionType {

    @XmlElement(required = true)
    protected String target;
    @XmlElement(required = true)
    protected StatechangeType statechange;

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

    /**
     * Gets the value of the statechange property.
     * 
     * @return
     *     possible object is
     *     {@link StatechangeType }
     *     
     */
    public StatechangeType getStatechange() {
        return statechange;
    }

    /**
     * Sets the value of the statechange property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatechangeType }
     *     
     */
    public void setStatechange(StatechangeType value) {
        this.statechange = value;
    }

}
