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

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;

import org.uncertml.libuncertml.IErrorHandler;
import org.uncertml.libuncertml.IMarshaller;
import org.uncertml.libuncertml.dom.UncertMLType;


public class MarshallerImpl implements IMarshaller {
	private static final String CONTEXT_NAME = "org.uncertml.libpharmml.dom";
	private IErrorHandler errorHandler;
	
	public MarshallerImpl(){
	}
	
	@Override
	public void marshall(JAXBElement<UncertMLType> dom, OutputStream os) {
		try {
			JAXBContext context = JAXBContext.newInstance(CONTEXT_NAME);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(dom, os);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JAXBElement<UncertMLType> unmarshall(InputStream is) {
		try {
			Schema mySchema = PharmMLSchemaFactory.getInstance().createPharmMlSchema();
			JAXBContext context = JAXBContext.newInstance(CONTEXT_NAME);
			Unmarshaller u = context.createUnmarshaller();
			u.setEventHandler(new ValidationEventHandler() {
				
				@Override
				public boolean handleEvent(ValidationEvent event) {
					int severity = event.getSeverity();
					switch(severity){
					case ValidationEvent.ERROR:
						errorHandler.handleError(event.getMessage());
						break;
					case ValidationEvent.FATAL_ERROR:
						errorHandler.handleError(event.getMessage());
						break;
					case ValidationEvent.WARNING:
						errorHandler.handleWarning(event.getMessage());
						break;
					}
					return true;
				}
			});
			u.setSchema(mySchema);
			@SuppressWarnings("unchecked")
			JAXBElement<UncertMLType> doc = (JAXBElement<UncertMLType>)u.unmarshal(is);
			return doc;
		} catch (JAXBException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void setErrorHandler(IErrorHandler repFact) {
		this.errorHandler = repFact;
	}

	@Override
	public IErrorHandler getErrorHandler() {
		return this.errorHandler;
	}

}
