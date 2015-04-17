package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.jboss.tools.batch.ui.editor.internal.services.contentproposal.JobListenerRefProposalService;

/**
 * Represents {@code <listener>} child tags of {@code <listeners>} the parent of
 * which is a {@code <job>}.
 * 
 * @author Tomas Milata
 *
 */
public interface JobListener extends Listener {
	ElementType TYPE = new ElementType(JobListener.class);

	@Label(standard = "ref")
	@XmlBinding(path = "@ref")
	@Required
	@Service(impl = JobListenerRefProposalService.class)
	ValueProperty PROP_REF = new ValueProperty(TYPE, "Ref");

	Value<String> getRef();

	void setRef(String ref);
}
