//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:00:21 PM CET 
//


package org.kit.scufl.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Encoding.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Encoding">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="xstream"/>
 *     &lt;enumeration value="dataflow"/>
 *     &lt;enumeration value="jdomxml"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Encoding")
@XmlEnum
public enum Encoding {

    @XmlEnumValue("xstream")
    XSTREAM("xstream"),
    @XmlEnumValue("dataflow")
    DATAFLOW("dataflow"),
    @XmlEnumValue("jdomxml")
    JDOMXML("jdomxml");
    private final String value;

    Encoding(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Encoding fromValue(String v) {
        for (Encoding c: Encoding.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
