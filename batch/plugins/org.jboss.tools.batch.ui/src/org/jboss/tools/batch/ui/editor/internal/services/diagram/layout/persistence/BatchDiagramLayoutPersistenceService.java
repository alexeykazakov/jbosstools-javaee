package org.jboss.tools.batch.ui.editor.internal.services.diagram.layout.persistence;

import org.eclipse.sapphire.ui.diagram.DiagramConnectionPart;
import org.eclipse.sapphire.ui.diagram.layout.DiagramLayoutPersistenceService;

/**
 * Overrides Sapphire's standard diagram layout persistence so that no
 * persistence is used. Auto layout is used when file is opened.
 * 
 * @author Tomas Milata
 */
public class BatchDiagramLayoutPersistenceService extends DiagramLayoutPersistenceService {

	@Override
	public DiagramConnectionInfo read(DiagramConnectionPart connection) {
		return null;
	}

}
