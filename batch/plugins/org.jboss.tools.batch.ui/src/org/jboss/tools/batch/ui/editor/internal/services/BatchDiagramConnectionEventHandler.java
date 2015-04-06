package org.jboss.tools.batch.ui.editor.internal.services;

import org.eclipse.sapphire.ui.diagram.ConnectionAddEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionDeleteEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionEndpointsEvent;

public interface BatchDiagramConnectionEventHandler {
	
	void onConnectionEndpointsEvent(ConnectionEndpointsEvent event);
	void onConnectionAddEvent(ConnectionAddEvent event);
	void onConnectionDeleteEvent(ConnectionDeleteEvent event);
}
