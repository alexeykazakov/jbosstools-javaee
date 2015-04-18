package org.jboss.tools.batch.ui.editor.internal.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.FilteredListener;
import org.eclipse.sapphire.PropertyContentEvent;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ui.diagram.ConnectionAddEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionDeleteEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionEndpointsEvent;
import org.eclipse.sapphire.ui.diagram.DiagramConnectionPart;
import org.eclipse.sapphire.ui.diagram.StandardConnectionService;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.eclipse.sapphire.ui.diagram.editor.SapphireDiagramEditorPagePart;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElement;
import org.jboss.tools.batch.ui.editor.internal.model.Job;
import org.jboss.tools.batch.ui.editor.internal.model.Split;
import org.jboss.tools.batch.ui.editor.internal.model.Step;

public class BatchDiagramConnectionService extends StandardConnectionService {

	private static final String NEXT_ATTRIBUTE_CONNECTION_ID = "NextAttributeConnection";

	private List<DiagramConnectionPart> connections;

	private SapphireDiagramEditorPagePart diagramPart;
	private EventHandler eventHandler = new EventHandler();

	@Override
	protected void init() {
		super.init();

		diagramPart = context(SapphireDiagramEditorPagePart.class);
	}

	private void initConnections() {
		connections = new ArrayList<>();

		Element root = diagramPart.getLocalModelElement();
		ElementList<FlowElement> children;
		if (root instanceof Job) {
			children = ((Job) root).getFlowElements();
		} else {
			children = ((Flow) root).getFlowElements();
		}

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
						connect(diagramPart.getDiagramNodePart(src), diagramPart.getDiagramNodePart(target),
								NEXT_ATTRIBUTE_CONNECTION_ID);
					}
				}
			}

//			src.attach(new FilteredListener<PropertyContentEvent>() { FIXME
//				@Override
//				protected void handleTypedEvent(final PropertyContentEvent event) {
//					System.out.println(event.toString());
//				}
//			}, "Next");

		}
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
			BatchDiagramConnectionPart connectionPart = new BatchDiagramConnectionPart(node1, node2, connectionType,
					diagramPart, eventHandler);

			connectionPart.init(diagramPart, node1.getLocalModelElement(),
					diagramPart.getDiagramConnectionDef(connectionType), Collections.<String, String> emptyMap());
			connectionPart.initialize();

			connections.add(connectionPart);
			return connectionPart;
		} else {
			return super.connect(node1, node2, connectionType);
		}
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
			BatchDiagramConnectionService.this.broadcast(event);
		}

	}

	// @Override
	// public List<IDiagramConnectionDef> possibleConnectionDefs(DiagramNodePart
	// srcNode) {
	// return
	// Collections.singletonList(diagramPart.getDiagramConnectionDef("NextConnection"));
	// // TODO
	// }
}
