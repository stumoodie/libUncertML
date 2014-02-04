/*******************************************************************************
 * Copyright (c) 2014 Eight Pillars Ltd,
 * Edinburgh, UK.
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on 
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
 * KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations 
 * under the License.
 *******************************************************************************/
package org.uncertml.libuncertml.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class PharmMLSchemaFactory {
	private static PharmMLSchemaFactory anInstance = null;

	private static final String PHARML_URI = "UncertML30.xsd";
	
	
	public static PharmMLSchemaFactory getInstance(){
		if(anInstance == null){
			anInstance = new PharmMLSchemaFactory();
		}
		return anInstance;
	}
	
	
	public Schema createPharmMlSchema(){
		try(InputStream in = this.getClass().getResourceAsStream(PHARML_URI)){
			StreamSource src = new StreamSource(in);
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema mySchema = sf.newSchema(src);

			return mySchema;
		} catch (IOException | SAXException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
