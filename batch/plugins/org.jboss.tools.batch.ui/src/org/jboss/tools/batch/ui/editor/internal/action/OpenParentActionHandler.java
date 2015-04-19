package org.jboss.tools.batch.ui.editor.internal.action;

import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.eclipse.sapphire.ui.swt.gef.presentation.DiagramPagePresentation;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElementsContainer;
import org.jboss.tools.batch.ui.editor.internal.model.JobXMLEditor;
import org.jboss.tools.batch.ui.editor.internal.model.Split;

public class OpenParentActionHandler extends SapphireActionHandler {

	@Override
	protected Object run(Presentation context) {
		DiagramPagePresentation diagramNodePresentation = (DiagramPagePresentation) context;
		JobXMLEditor editor = (JobXMLEditor) diagramNodePresentation.getConfigurationManager().getDiagramEditor().getEditor();

		Flow flow = (Flow) context.part().getModelElement();	
		
		FlowElementsContainer parent = (FlowElementsContainer) flow.parent().element();
		if (parent instanceof Split) { // If the flow belongs to a split, we want to open split's parent.
			parent = (FlowElementsContainer) parent.parent().element();
		}
		editor.changeDiagramContent(parent);
		
		return null;		
	}

}
