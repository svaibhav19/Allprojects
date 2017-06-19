package com.test.app;

import org.openrdf.annotations.Iri;

import com.github.anno4j.model.Body;
import com.github.anno4j.model.namespaces.DC;
import com.github.anno4j.model.namespaces.RDF;

public interface TextAnnotationBody extends Body{
	
	@Iri(DC.FORMAT)  
    String getFormat();
    
    @Iri(DC.FORMAT)
    void setFormat(String format);

    @Iri(RDF.VALUE)
    String getValue();

    @Iri(RDF.VALUE)
    void setValue(String value);
    
    @Iri(DC.LANGUAGE)
    String getLanguage();

    @Iri(DC.LANGUAGE)
    void setLanguage(String language);

}
