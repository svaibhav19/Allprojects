//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.09 at 03:17:22 PM CET 
//


package org.kit.xscufl.api;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for scuflType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scuflType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="workflowdescription" type="{http://org.embl.ebi.escience/xscufl/0.1alpha}workflowdescriptionType"/>
 *         &lt;element name="processor" type="{http://org.embl.ebi.escience/xscufl/0.1alpha}processorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="link" type="{http://org.embl.ebi.escience/xscufl/0.1alpha}linkType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="source" type="{http://org.embl.ebi.escience/xscufl/0.1alpha}sourceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sink" type="{http://org.embl.ebi.escience/xscufl/0.1alpha}sinkType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="coordination" type="{http://org.embl.ebi.escience/xscufl/0.1alpha}coordinationType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="log" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scuflType", propOrder = {
    "workflowdescription",
    "processor",
    "link",
    "source",
    "sink",
    "coordination"
})
public class ScuflType {

    @XmlElement(required = true)
    protected WorkflowdescriptionType workflowdescription;
    protected List<ProcessorType> processor;
    protected List<LinkType> link;
    protected List<SourceType> source;
    protected List<SinkType> sink;
    protected List<CoordinationType> coordination;
    @XmlAttribute(name = "version")
    protected Float version;
    @XmlAttribute(name = "log")
    protected Byte log;

    /**
     * Gets the value of the workflowdescription property.
     * 
     * @return
     *     possible object is
     *     {@link WorkflowdescriptionType }
     *     
     */
    public WorkflowdescriptionType getWorkflowdescription() {
        return workflowdescription;
    }

    /**
     * Sets the value of the workflowdescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkflowdescriptionType }
     *     
     */
    public void setWorkflowdescription(WorkflowdescriptionType value) {
        this.workflowdescription = value;
    }

    /**
     * Gets the value of the processor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessorType }
     * 
     * 
     */
    public List<ProcessorType> getProcessor() {
        if (processor == null) {
            processor = new ArrayList<ProcessorType>();
        }
        return this.processor;
    }

    /**
     * Gets the value of the link property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the link property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLink().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinkType }
     * 
     * 
     */
    public List<LinkType> getLink() {
        if (link == null) {
            link = new ArrayList<LinkType>();
        }
        return this.link;
    }

    /**
     * Gets the value of the source property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the source property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SourceType }
     * 
     * 
     */
    public List<SourceType> getSource() {
        if (source == null) {
            source = new ArrayList<SourceType>();
        }
        return this.source;
    }

    /**
     * Gets the value of the sink property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sink property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSink().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SinkType }
     * 
     * 
     */
    public List<SinkType> getSink() {
        if (sink == null) {
            sink = new ArrayList<SinkType>();
        }
        return this.sink;
    }

    /**
     * Gets the value of the coordination property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coordination property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoordination().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CoordinationType }
     * 
     * 
     */
    public List<CoordinationType> getCoordination() {
        if (coordination == null) {
            coordination = new ArrayList<CoordinationType>();
        }
        return this.coordination;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setVersion(Float value) {
        this.version = value;
    }

    /**
     * Gets the value of the log property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getLog() {
        return log;
    }

    /**
     * Sets the value of the log property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setLog(Byte value) {
        this.log = value;
    }

}
