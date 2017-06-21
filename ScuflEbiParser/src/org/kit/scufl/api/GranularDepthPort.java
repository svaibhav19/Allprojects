//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:00:21 PM CET 
//


package org.kit.scufl.api;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GranularDepthPort complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GranularDepthPort">
 *   &lt;complexContent>
 *     &lt;extension base="{http://taverna.sf.net/2008/xml/t2flow}DepthPort">
 *       &lt;sequence>
 *         &lt;element name="granularDepth" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GranularDepthPort", propOrder = {
    "granularDepth"
})
@XmlSeeAlso({
    AnnotatedGranularDepthPort.class
})
public class GranularDepthPort
    extends DepthPort
{

    @XmlElement(namespace = "http://taverna.sf.net/2008/xml/t2flow", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger granularDepth;

    /**
     * Gets the value of the granularDepth property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGranularDepth() {
        return granularDepth;
    }

    /**
     * Sets the value of the granularDepth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGranularDepth(BigInteger value) {
        this.granularDepth = value;
    }

}