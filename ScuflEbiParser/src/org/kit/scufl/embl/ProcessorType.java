//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:01:34 PM CET 
//

package org.kit.scufl.embl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for processorType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processorType", propOrder = { "description", "defaults", "processorElement", "mergemode",
		"iterationstrategy", "alternate", "template" })
@XmlSeeAlso({ AlternateType.class })
public class ProcessorType {

	protected DescriptionType description;
	protected DefaultsType defaults;
	@XmlElementRef(name = "processorElement", namespace = "http://org.embl.ebi.escience/xscufl/0.1alpha", type = JAXBElement.class)
	protected JAXBElement<?> processorElement;
	protected List<MergemodeType> mergemode;
	protected IterationstrategyType iterationstrategy;
	protected List<AlternateType> alternate;
	@XmlElement(required = true)
	protected TemplateType template;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "workers")
	@XmlSchemaType(name = "positiveInteger")
	protected BigInteger workers;
	@XmlAttribute(name = "boring")
	protected Boolean boring;
	@XmlAttribute(name = "log")
	protected String log;

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link DescriptionType }
	 * 
	 */
	public DescriptionType getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link DescriptionType }
	 * 
	 */
	public void setDescription(DescriptionType value) {
		this.description = value;
	}

	/**
	 * Gets the value of the defaults property.
	 * 
	 * @return possible object is {@link DefaultsType }
	 * 
	 */
	public DefaultsType getDefaults() {
		return defaults;
	}

	/**
	 * Sets the value of the defaults property.
	 * 
	 * @param value
	 *            allowed object is {@link DefaultsType }
	 * 
	 */
	public void setDefaults(DefaultsType value) {
		this.defaults = value;
	}

	/**
	 * Gets the value of the processorElement property.
	 * 
	 * @return possible object is {@link JAXBElement
	 *         }{@code <}{@link SoaplabwsdlType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link BeanshellType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link Object }{@code >} {@link JAXBElement
	 *         }{@code <}{@link BiomobyparserType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link StringconstantType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link InfernoType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link BiomobyobjectType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link BiomartType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link ArbitraryWsdlType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link NotificationType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link RshellType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link LocalType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link BiomobywsdlType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link ApiconsumerType }{@code >} {@link JAXBElement
	 *         }{@code <}{@link AbstractprocessorType }{@code >}
	 * 
	 */
	public JAXBElement<?> getProcessorElement() {
		return processorElement;
	}

	/**
	 * Sets the value of the processorElement property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement
	 *            }{@code <}{@link SoaplabwsdlType }{@code >} {@link JAXBElement
	 *            }{@code <}{@link BeanshellType }{@code >} {@link JAXBElement
	 *            }{@code <}{@link Object }{@code >} {@link JAXBElement
	 *            }{@code <}{@link BiomobyparserType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link StringconstantType
	 *            }{@code >} {@link JAXBElement }{@code <}{@link InfernoType
	 *            }{@code >} {@link JAXBElement
	 *            }{@code <}{@link BiomobyobjectType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link BiomartType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link ArbitraryWsdlType
	 *            }{@code >} {@link JAXBElement
	 *            }{@code <}{@link NotificationType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link RshellType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link LocalType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link BiomobywsdlType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link ApiconsumerType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link AbstractprocessorType
	 *            }{@code >}
	 * 
	 */
	public void setProcessorElement(JAXBElement<?> value) {
		this.processorElement = value;
	}

	/**
	 * Gets the value of the mergemode property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the mergemode property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMergemode().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link MergemodeType }
	 * 
	 * 
	 */
	public List<MergemodeType> getMergemode() {
		if (mergemode == null) {
			mergemode = new ArrayList<MergemodeType>();
		}
		return this.mergemode;
	}

	/**
	 * Gets the value of the iterationstrategy property.
	 * 
	 * @return possible object is {@link IterationstrategyType }
	 * 
	 */
	public IterationstrategyType getIterationstrategy() {
		return iterationstrategy;
	}

	/**
	 * Sets the value of the iterationstrategy property.
	 * 
	 * @param value
	 *            allowed object is {@link IterationstrategyType }
	 * 
	 */
	public void setIterationstrategy(IterationstrategyType value) {
		this.iterationstrategy = value;
	}

	/**
	 * Gets the value of the alternate property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the alternate property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAlternate().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AlternateType }
	 * 
	 * 
	 */
	public List<AlternateType> getAlternate() {
		if (alternate == null) {
			alternate = new ArrayList<AlternateType>();
		}
		return this.alternate;
	}

	/**
	 * Gets the value of the template property.
	 * 
	 * @return possible object is {@link TemplateType }
	 * 
	 */
	public TemplateType getTemplate() {
		return template;
	}

	/**
	 * Sets the value of the template property.
	 * 
	 * @param value
	 *            allowed object is {@link TemplateType }
	 * 
	 */
	public void setTemplate(TemplateType value) {
		this.template = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the workers property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getWorkers() {
		return workers;
	}

	/**
	 * Sets the value of the workers property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setWorkers(BigInteger value) {
		this.workers = value;
	}

	/**
	 * Gets the value of the boring property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isBoring() {
		return boring;
	}

	/**
	 * Sets the value of the boring property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setBoring(Boolean value) {
		this.boring = value;
	}

	/**
	 * Gets the value of the log property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLog() {
		return log;
	}

	/**
	 * Sets the value of the log property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLog(String value) {
		this.log = value;
	}

}