//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.05 at 09:01:34 PM CET 
//


package org.kit.scufl.embl;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class BiomobyParameter
    extends JAXBElement<BiomobyParameterType>
{

    protected final static QName NAME = new QName("http://org.embl.ebi.escience/xscufl/0.1alpha", "Parameter");

    public BiomobyParameter(BiomobyParameterType value) {
        super(NAME, ((Class) BiomobyParameterType.class), null, value);
    }

    public BiomobyParameter() {
        super(NAME, ((Class) BiomobyParameterType.class), null, null);
    }

}
