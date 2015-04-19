package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

public interface FlowElementsContainer extends Element {

	ElementType TYPE = new ElementType(FlowElementsContainer.class);
	
	@Type( base = FlowElement.class,
			possible = {
				Flow.class,
				Step.class,
				Decision.class,
				Split.class
			}
	)
	@XmlListBinding( path = "" )
	ListProperty PROP_FLOW_ELEMENTS = new ListProperty(TYPE, "FlowElements");

	ElementList<FlowElement> getFlowElements();

}
