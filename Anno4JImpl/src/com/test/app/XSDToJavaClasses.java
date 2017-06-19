package com.test.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xml.sax.InputSource;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

public class XSDToJavaClasses {
	
	public static void main(String[] args) {
		File schemaFile = new File("E:\\temp\\temp.xsd");
		System.out.println(schemaFile.exists());
		SchemaCompiler sc = XJC.createSchemaCompiler();
		InputSource is;
		try {
			is = new InputSource(new FileInputStream(schemaFile));
			String path = URLEncoder.encode(schemaFile.toURI().toString(), "UTF-8");
	        is.setSystemId(path);
			sc.parseSchema(is);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        S2JJAXBModel model = sc.bind();
        JCodeModel jCodeModel = model.generateCode(null, null);
        String outputDirectory = "E:\\temp\\";
        try {
			jCodeModel.build(new File(outputDirectory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
