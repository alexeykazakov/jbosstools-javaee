package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.jboss.tools.batch.ui.editor.internal.services.contentproposal.RefProposalService;

public interface RefAttributeElement {
	ElementType TYPE = new ElementType(RefAttributeElement.class);

	@Label(standard = "ref")
	@XmlBinding(path = "@ref")
	@Required
	@Service(impl = RefProposalService.class)
	ValueProperty PROP_REF = new ValueProperty(TYPE, "Ref");

	Value<String> getRef();

	void setRef(String ref);
}
