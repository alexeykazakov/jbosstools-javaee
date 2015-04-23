package org.jboss.tools.batch.ui.editor.internal.action;

import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElementsContainer;
import org.jboss.tools.batch.ui.editor.internal.model.NextAttributeElement;

public class SetStartActionHandler extends SapphireActionHandler {

	@Override
	protected Object run(Presentation context) {
		
		NextAttributeElement element = (NextAttributeElement) context.part().getModelElement();		
		FlowElementsContainer parent = (FlowElementsContainer) element.parent().element();
		
		parent.getFlowElements().move(element, 0);
				
		return null;
	}

}
