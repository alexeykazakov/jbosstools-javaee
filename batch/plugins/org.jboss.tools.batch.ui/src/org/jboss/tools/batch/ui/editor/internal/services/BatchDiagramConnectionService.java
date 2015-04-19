package org.jboss.tools.batch.ui.editor.internal.services;

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
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ui.diagram.ConnectionAddEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionDeleteEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionEndpointsEvent;
import org.eclipse.sapphire.ui.diagram.DiagramConnectionPart;
import org.eclipse.sapphire.ui.diagram.StandardConnectionService;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.eclipse.sapphire.ui.diagram.editor.SapphireDiagramEditorPagePart;
import org.jboss.tools.batch.ui.editor.internal.model.Decision;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElement;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElementsContainer;
import org.jboss.tools.batch.ui.editor.internal.model.Job;
import org.jboss.tools.batch.ui.editor.internal.model.Split;
import org.jboss.tools.batch.ui.editor.internal.model.Step;

public class BatchDiagramConnectionService extends StandardConnectionService {

	private static final String NEXT_ATTRIBUTE_CONNECTION_ID = "NextAttributeConnection";

	private List<DiagramConnectionPart> connections;

	private Map<Element, Element> nodesConnectionsMap = new HashMap<>();

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
			Value<String> next = null;
			if (src instanceof Step) {
				next = ((Step) src).getNext();
			} else if (src instanceof Flow) {
				next = ((Flow) src).getNext();
			} else if (src instanceof Split) {
				next = ((Split) src).getNext();
			}
			if (next != null && next.content() != null && !next.content().trim().isEmpty()) {
				for (FlowElement target : children) {
					String targetId = target.getId().content();
					if (targetId != null && targetId.equals(next.content())) {
						addConnectionPart(diagramPart.getDiagramNodePart(src), diagramPart.getDiagramNodePart(target));
					}
				}
			}

			if (!(src instanceof Decision)) {
				attachListenerForNewConnection(src);
			}

		}
	}

	private void attachListenerForNewConnection(final FlowElement flowElement) {
		flowElement.attach(new FilteredListener<PropertyContentEvent>() {
			@Override
			protected void handleTypedEvent(final PropertyContentEvent event) {
				System.out.println(event.toString());
				ReferenceValue<String, FlowElement> next = null;
				if (flowElement instanceof Step) {
					next = ((Step) flowElement).getNext();
				} else if (flowElement instanceof Flow) {
					next = ((Flow) flowElement).getNext();
				} else if (flowElement instanceof Split) {
					next = ((Split) flowElement).getNext();
				}
				if (!nodesConnectionsMap.containsKey(flowElement)) {
					addConnectionPart(diagramPart.getDiagramNodePart(flowElement),
							diagramPart.getDiagramNodePart(next.target()));
				}
			}
		}, "Next");
	}

	@Override
	public boolean valid(DiagramNodePart node1, DiagramNodePart node2, String connectionType) {
		if (connectionType.equals(NEXT_ATTRIBUTE_CONNECTION_ID)) {
			return true; // TODO
		} else {
			return super.valid(node1, node2, connectionType);
		}
	}

	@Override
	public DiagramConnectionPart connect(DiagramNodePart node1, DiagramNodePart node2, String connectionType) {

		if (connectionType.equals(NEXT_ATTRIBUTE_CONNECTION_ID)) {

			// connect the reference in the model
			String nextId = ((FlowElement) node2.getLocalModelElement()).getId().content();
			Element element = node1.getLocalModelElement();
			if (element instanceof Step) {
				((Step) element).setNext(nextId);
			} else if (element instanceof Flow) {
				((Flow) element).setNext(nextId);
			} else if (element instanceof Split) {
				((Split) element).setNext(nextId);
			} else {
				throw new IllegalArgumentException(); // TODO
			}

			return addConnectionPart(node1, node2);
		} else {
			return super.connect(node1, node2, connectionType);
		}
	}

	private DiagramConnectionPart addConnectionPart(DiagramNodePart node1, DiagramNodePart node2) {
		BatchDiagramConnectionPart connectionPart = new BatchDiagramConnectionPart(node1, node2,
				NEXT_ATTRIBUTE_CONNECTION_ID, diagramPart, eventHandler);

		connectionPart.init(diagramPart, node1.getLocalModelElement(),
				diagramPart.getDiagramConnectionDef(NEXT_ATTRIBUTE_CONNECTION_ID),
				Collections.<String, String> emptyMap());
		connectionPart.initialize();

		nodesConnectionsMap.put(node1.getLocalModelElement(), node2.getLocalModelElement());
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
