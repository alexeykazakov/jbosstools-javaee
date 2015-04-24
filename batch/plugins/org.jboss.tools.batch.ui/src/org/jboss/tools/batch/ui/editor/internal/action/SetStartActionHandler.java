package org.jboss.tools.batch.ui.editor.internal.action;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElementsContainer;

public class SetStartActionHandler extends SapphireActionHandler {

	@Override
	protected Object run(Presentation context) {
		
		Element element = context.part().getModelElement();		
		FlowElementsContainer parent = (FlowElementsContainer) element.parent().element();
		
		parent.getFlowElements().move(element, 0);
				
		return null;
	}

}
