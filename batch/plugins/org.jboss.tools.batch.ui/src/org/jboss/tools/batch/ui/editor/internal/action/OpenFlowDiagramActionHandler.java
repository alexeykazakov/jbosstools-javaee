package org.jboss.tools.batch.ui.editor.internal.action;

import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.eclipse.sapphire.ui.swt.gef.presentation.DiagramNodePresentation;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.JobXMLEditor;

public class OpenFlowDiagramActionHandler extends SapphireActionHandler {

	@Override
	protected Object run(Presentation context) {
		DiagramNodePresentation diagramNodePresentation = (DiagramNodePresentation) context;
		JobXMLEditor editor = (JobXMLEditor) diagramNodePresentation.getConfigurationManager().getDiagramEditor().getEditor();
		
		Flow flow = (Flow) context.part().getModelElement();		
		editor.changeDiagramContent(flow);
				
		return null;
	}

}
