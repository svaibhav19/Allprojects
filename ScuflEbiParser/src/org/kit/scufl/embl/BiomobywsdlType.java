//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:01:34 PM CET 
//


package org.kit.scufl.embl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for biomobywsdlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="biomobywsdlType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}mobyEndpoint"/>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}serviceName"/>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}authorityName"/>
 *         &lt;element ref="{http://org.embl.ebi.escience/xscufl/0.1alpha}Parameter" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "biomobywsdlType", propOrder = {
    "mobyEndpoint",
    "serviceName",
    "authorityName",
    "parameter"
})
public class BiomobywsdlType {

    @XmlElement(required = true)
    protected MobyEndpointType mobyEndpoint;
    @XmlElement(required = true)
    protected ServiceNameType serviceName;
    @XmlElement(required = true)
    protected AuthorityNameType authorityName;
    @XmlElementRef(name = "Parameter", namespace = "http://org.embl.ebi.escience/xscufl/0.1alpha", type = BiomobyParameter.class)
    protected List<BiomobyParameter> parameter;

    /**
     * Gets the value of the mobyEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link MobyEndpointType }
     *     
     */
    public MobyEndpointType getMobyEndpoint() {
        return mobyEndpoint;
    }

    /**
     * Sets the value of the mobyEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link MobyEndpointType }
     *     
     */
    public void setMobyEndpoint(MobyEndpointType value) {
        this.mobyEndpoint = value;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceNameType }
     *     
     */
    public ServiceNameType getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceNameType }
     *     
     */
    public void setServiceName(ServiceNameType value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the authorityName property.
     * 
     * @return
     *     possible object is
     *     {@link AuthorityNameType }
     *     
     */
    public AuthorityNameType getAuthorityName() {
        return authorityName;
    }

    /**
     * Sets the value of the authorityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorityNameType }
     *     
     */
    public void setAuthorityName(AuthorityNameType value) {
        this.authorityName = value;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BiomobyParameter }
     * 
     * 
     */
    public List<BiomobyParameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<BiomobyParameter>();
        }
        return this.parameter;
    }

}