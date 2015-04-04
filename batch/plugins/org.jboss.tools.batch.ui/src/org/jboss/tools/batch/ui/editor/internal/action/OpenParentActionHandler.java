package org.jboss.tools.batch.ui.editor.internal.action;

import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.eclipse.sapphire.ui.swt.gef.presentation.DiagramPagePresentation;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.JobXMLEditor;

public class OpenParentActionHandler extends SapphireActionHandler {

	@Override
	protected Object run(Presentation context) {
		DiagramPagePresentation diagramNodePresentation = (DiagramPagePresentation) context;
		JobXMLEditor editor = (JobXMLEditor) diagramNodePresentation.getConfigurationManager().getDiagramEditor().getEditor();

		Flow flow = (Flow) context.part().getModelElement();				
		editor.goUp(flow);
		
		return null;		
	}

}
