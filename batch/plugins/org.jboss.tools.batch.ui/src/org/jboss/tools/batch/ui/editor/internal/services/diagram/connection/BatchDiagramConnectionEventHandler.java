package org.jboss.tools.batch.ui.editor.internal.services.diagram.connection;

import org.eclipse.sapphire.ui.diagram.ConnectionAddEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionDeleteEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionEndpointsEvent;

/**
 * Interface for listening to and handling of events in the lifecycle of a batch
 * diagram connection.
 * 
 * @author Tomas Milata
 */
interface BatchDiagramConnectionEventHandler {

	/**
	 * Is called when one of connection endpoints is changed.
	 */
	void onConnectionEndpointsEvent(ConnectionEndpointsEvent event);

	/**
	 * Is called when a new connection is added.
	 */
	void onConnectionAddEvent(ConnectionAddEvent event);

	/**
	 * Is called when a connection is deleted.
	 */
	void onConnectionDeleteEvent(ConnectionDeleteEvent event);
}
