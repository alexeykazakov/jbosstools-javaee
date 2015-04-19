package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementReference;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ReferenceValue;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Reference;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

public interface NextAttributeElement extends Element {

	ElementType TYPE = new ElementType( NextAttributeElement.class );
	
	@Label( standard = "next" )
	@XmlBinding( path = "@next" )
	@Required
	@Reference(target = FlowElement.class)
	@ElementReference(list = "../FlowElements" , key = "id")

	ValueProperty PROP_NEXT = new ValueProperty( TYPE, "Next" );

	ReferenceValue<String, FlowElement> getNext();
	void setNext( String value);
}
