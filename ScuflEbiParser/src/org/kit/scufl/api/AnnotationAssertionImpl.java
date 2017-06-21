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
 * <p>Java class for AnnotationAssertionImpl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnnotationAssertionImpl">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="net.sf.taverna.t2.annotation.AnnotationAssertionImpl" form="unqualified">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="annotationBean" type="{http://taverna.sf.net/2008/xml/t2flow}AnnotationBean" form="unqualified"/>
 *                   &lt;element name="date" type="{http://taverna.sf.net/2008/xml/t2flow}datetime" form="unqualified"/>
 *                   &lt;element name="creators" type="{http://www.w3.org/2001/XMLSchema}anyType" form="unqualified"/>
 *                   &lt;element name="curationEventList" type="{http://www.w3.org/2001/XMLSchema}anyType" form="unqualified"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnnotationAssertionImpl", propOrder = {
    "netSfTavernaT2AnnotationAnnotationAssertionImpl"
})
public class AnnotationAssertionImpl {

    @XmlElement(name = "net.sf.taverna.t2.annotation.AnnotationAssertionImpl", required = true)
    protected AnnotationAssertionImpl.NetSfTavernaT2AnnotationAnnotationAssertionImpl netSfTavernaT2AnnotationAnnotationAssertionImpl;

    /**
     * Gets the value of the netSfTavernaT2AnnotationAnnotationAssertionImpl property.
     * 
     * @return
     *     possible object is
     *     {@link AnnotationAssertionImpl.NetSfTavernaT2AnnotationAnnotationAssertionImpl }
     *     
     */
    public AnnotationAssertionImpl.NetSfTavernaT2AnnotationAnnotationAssertionImpl getNetSfTavernaT2AnnotationAnnotationAssertionImpl() {
        return netSfTavernaT2AnnotationAnnotationAssertionImpl;
    }

    /**
     * Sets the value of the netSfTavernaT2AnnotationAnnotationAssertionImpl property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnnotationAssertionImpl.NetSfTavernaT2AnnotationAnnotationAssertionImpl }
     *     
     */
    public void setNetSfTavernaT2AnnotationAnnotationAssertionImpl(AnnotationAssertionImpl.NetSfTavernaT2AnnotationAnnotationAssertionImpl value) {
        this.netSfTavernaT2AnnotationAnnotationAssertionImpl = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="annotationBean" type="{http://taverna.sf.net/2008/xml/t2flow}AnnotationBean" form="unqualified"/>
     *         &lt;element name="date" type="{http://taverna.sf.net/2008/xml/t2flow}datetime" form="unqualified"/>
     *         &lt;element name="creators" type="{http://www.w3.org/2001/XMLSchema}anyType" form="unqualified"/>
     *         &lt;element name="curationEventList" type="{http://www.w3.org/2001/XMLSchema}anyType" form="unqualified"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "annotationBean",
        "date",
        "creators",
        "curationEventList"
    })
    public static class NetSfTavernaT2AnnotationAnnotationAssertionImpl {

        @XmlElement(required = true)
        protected AnnotationBean annotationBean;
        @XmlElement(required = true)
        protected String date;
        @XmlElement(required = true)
        protected Object creators;
        @XmlElement(required = true)
        protected Object curationEventList;

        /**
         * Gets the value of the annotationBean property.
         * 
         * @return
         *     possible object is
         *     {@link AnnotationBean }
         *     
         */
        public AnnotationBean getAnnotationBean() {
            return annotationBean;
        }

        /**
         * Sets the value of the annotationBean property.
         * 
         * @param value
         *     allowed object is
         *     {@link AnnotationBean }
         *     
         */
        public void setAnnotationBean(AnnotationBean value) {
            this.annotationBean = value;
        }

        /**
         * Gets the value of the date property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDate() {
            return date;
        }

        /**
         * Sets the value of the date property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDate(String value) {
            this.date = value;
        }

        /**
         * Gets the value of the creators property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getCreators() {
            return creators;
        }

        /**
         * Sets the value of the creators property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setCreators(Object value) {
            this.creators = value;
        }

        /**
         * Gets the value of the curationEventList property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getCurationEventList() {
            return curationEventList;
        }

        /**
         * Sets the value of the curationEventList property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setCurationEventList(Object value) {
            this.curationEventList = value;
        }

    }

}
