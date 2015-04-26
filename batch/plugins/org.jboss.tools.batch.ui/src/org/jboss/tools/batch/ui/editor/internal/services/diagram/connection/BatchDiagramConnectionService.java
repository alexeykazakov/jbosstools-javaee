package org.jboss.tools.batch.ui.editor.internal.services.diagram.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.PropertyContentEvent;
import org.eclipse.sapphire.ReferenceValue;
import org.eclipse.sapphire.ui.diagram.ConnectionAddEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionDeleteEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionEndpointsEvent;
import org.eclipse.sapphire.ui.diagram.DiagramConnectionPart;
import org.eclipse.sapphire.ui.diagram.StandardConnectionService;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.eclipse.sapphire.ui.diagram.editor.SapphireDiagramEditorPagePart;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElement;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElementsContainer;
import org.jboss.tools.batch.ui.editor.internal.model.NextAttributeElement;

public class BatchDiagramConnectionService extends StandardConnectionService {

	private List<DiagramConnectionPart> connections;

	private Map<NextAttributeElement, FlowElement> nodesConnectionsMap = new HashMap<>();

	private SapphireDiagramEditorPagePart diagramPart;
	private EventHandler eventHandler = new EventHandler();

	@Override
	protected void init() {
		super.init();

		diagramPart = context(SapphireDiagramEditorPagePart.class);
	}

	private void initConnections() {
		connections = new ArrayList<>();

		ElementList<FlowElement> children = ((FlowElementsContainer) diagramPart.getLocalModelElement())
				.getFlowElements();

		for (FlowElement src : children) {
			if (src instanceof NextAttributeElement) {
				NextAttributeElement nextElem = (NextAttributeElement) src;
				ReferenceValue<String, FlowElement> next = nextElem.getNext();

				if (next.target() != null) {
					addConnectionPart(diagramPart.getDiagramNodePart(src),
							diagramPart.getDiagramNodePart(next.target()));
				}

				attachListenerForNewConnection(nextElem);
			}

		}
	}

	private void attachListenerForNewConnection(final NextAttributeElement element) {
		element.attach(new FilteredListener<PropertyContentEvent>() {
			@Override
			protected void handleTypedEvent(final PropertyContentEvent event) {
				if (!nodesConnectionsMap.containsKey(element)) {
					addConnectionPart(diagramPart.getDiagramNodePart(element),
							diagramPart.getDiagramNodePart(element.getNext().target()));
				}
			}
		}, NextAttributeElement.PROP_NEXT.name());
	}

	@Override
	public boolean valid(DiagramNodePart node1, DiagramNodePart node2, String connectionType) {
		if (connectionType.equals(BachConnectionIdConst.NEXT_ATTRIBUTE_CONNECTION_ID)) {
	
			Element src = node1.getLocalModelElement();
			if (!(src instanceof NextAttributeElement)) {
				return false;
			}

			FlowElement target = (FlowElement) node2.getLocalModelElement();
			if (target.getId().empty()) {
				// target must have id, otherwise there is nothing to write to
				// xml
				return false;
			}
			
			if (src.equals(target)) {
				return false; // no self-loop
			}
			
			FlowElement existingConnectionTarget = nodesConnectionsMap.get(src);
			// true if connection does not exist yet
			return existingConnectionTarget == null || !existingConnectionTarget.equals(target);
		} else {
			return super.valid(node1, node2, connectionType);
		}
	}

	@Override
	public DiagramConnectionPart connect(DiagramNodePart node1, DiagramNodePart node2, String connectionType) {

		if (connectionType.equals(BachConnectionIdConst.NEXT_ATTRIBUTE_CONNECTION_ID)) {

			// connect the reference in the model
			String nextId = ((FlowElement) node2.getLocalModelElement()).getId().content();
			NextAttributeElement element = (NextAttributeElement) node1.getLocalModelElement();
			element.setNext(nextId);

			FlowElement existingEndpoint = nodesConnectionsMap.get(node1.getLocalModelElement());
			if (existingEndpoint == null) {
				return addConnectionPart(node1, node2);
			} else {
				return null;
			}
		} else {
			return super.connect(node1, node2, connectionType);
		}
	}

	private DiagramConnectionPart addConnectionPart(DiagramNodePart node1, DiagramNodePart node2) {

		BatchDiagramConnectionPart connectionPart = new BatchDiagramConnectionPart(node1, node2, this, eventHandler);
		connectionPart.init(diagramPart, node1.getLocalModelElement(),
				diagramPart.getDiagramConnectionDef(BachConnectionIdConst.NEXT_ATTRIBUTE_CONNECTION_ID),
				Collections.<String, String> emptyMap());
		connectionPart.initialize();

		nodesConnectionsMap.put((NextAttributeElement) node1.getLocalModelElement(),
				(FlowElement) node2.getLocalModelElement());
		connections.add(connectionPart);
		return connectionPart;
	}

	@Override
	public List<DiagramConnectionPart> list() {
		List<DiagramConnectionPart> allConnections = new ArrayList<>();

		if (connections == null) {
			initConnections();
		}
		allConnections.addAll(connections);
		allConnections.addAll(super.list());

		return allConnections;
	}

	private final class EventHandler implements BatchDiagramConnectionEventHandler {

		@Override
		public void onConnectionEndpointsEvent(ConnectionEndpointsEvent event) {
			nodesConnectionsMap.put((NextAttributeElement) event.part().getEndpoint1(),
					(FlowElement) event.part().getEndpoint2());
			BatchDiagramConnectionService.this.broadcast(event);
		}

		@Override
		public void onConnectionAddEvent(ConnectionAddEvent event) {
			BatchDiagramConnectionService.this.broadcast(event);
		}

		@Override
		public void onConnectionDeleteEvent(ConnectionDeleteEvent event) {
			connections.remove(event.part());
			nodesConnectionsMap.remove(event.part().getEndpoint1());
			BatchDiagramConnectionService.this.broadcast(event);
		}

	}
}
