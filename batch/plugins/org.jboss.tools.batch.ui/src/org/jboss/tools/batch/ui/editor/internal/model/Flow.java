/*************************************************************************************
 * Copyright (c) 2014 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementReference;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.ReferenceValue;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Image;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Reference;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Services;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;
import org.jboss.tools.batch.ui.editor.internal.services.NextPossibleValuesService;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
@Label( standard = "flow" )
@Image ( path = "flow.png" )
@XmlBinding( path = "flow" )
public interface Flow extends FlowElement, FlowElementsContainer {
	
	ElementType TYPE = new ElementType( Flow.class );
    
	@Label( standard = "next" )
	@XmlBinding( path = "@next" )
	@Required
	@Reference(target = FlowElement.class)
	@ElementReference(list = "../FlowElements" , key = "id")

	ValueProperty PROP_NEXT = new ValueProperty( TYPE, "Next" );

	ReferenceValue<String, FlowElement> getNext();
	void setNext( String value);

	
	@Type( base = OutcomeElement.class,
			possible = {
				End.class,
				Fail.class,
				Stop.class,
				Next.class
			}
	)
	@XmlListBinding( path = "" )
	ListProperty PROP_OUTCOME_ELEMENTS = new ListProperty(TYPE, "OutcomeElements");

	ElementList<OutcomeElement> getOutcomeElements();
		
	@Type( base = Next.class )
	@XmlListBinding( path = "" )
	ListProperty PROP_NEXT_ELEMENTS = new ListProperty(TYPE, "NextElements");

	ElementList<Next> getNextElements();
	
	@Type( base = OutcomeElement.class,
			possible = {
				End.class,
				Fail.class,
				Stop.class
			}
	)
	@XmlListBinding( path = "" )
	ListProperty PROP_TERMINATING_ELEMENTS = new ListProperty(TYPE, "TerminatingElements");

	ElementList<OutcomeElement> getTerminatingElements();
	
	
}
