//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:01:34 PM CET 
//


package org.kit.scufl.embl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for elementtypeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="elementtypeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}complextype"/>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}arraytype"/>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}basetype"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "elementtypeType", propOrder = {
    "complextype",
    "arraytype",
    "basetype"
})
public class ElementtypeType {

    protected ComplextypeType complextype;
    protected ArraytypeType arraytype;
    protected BasetypeType basetype;

    /**
     * Gets the value of the complextype property.
     * 
     * @return
     *     possible object is
     *     {@link ComplextypeType }
     *     
     */
    public ComplextypeType getComplextype() {
        return complextype;
    }

    /**
     * Sets the value of the complextype property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplextypeType }
     *     
     */
    public void setComplextype(ComplextypeType value) {
        this.complextype = value;
    }

    /**
     * Gets the value of the arraytype property.
     * 
     * @return
     *     possible object is
     *     {@link ArraytypeType }
     *     
     */
    public ArraytypeType getArraytype() {
        return arraytype;
    }

    /**
     * Sets the value of the arraytype property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArraytypeType }
     *     
     */
    public void setArraytype(ArraytypeType value) {
        this.arraytype = value;
    }

    /**
     * Gets the value of the basetype property.
     * 
     * @return
     *     possible object is
     *     {@link BasetypeType }
     *     
     */
    public BasetypeType getBasetype() {
        return basetype;
    }

    /**
     * Sets the value of the basetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasetypeType }
     *     
     */
    public void setBasetype(BasetypeType value) {
        this.basetype = value;
    }

}
