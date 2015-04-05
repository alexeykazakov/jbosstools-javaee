package org.jboss.tools.batch.ui.editor.internal.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.Property;
import org.eclipse.sapphire.ReferenceValue;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.internal.DeclarativeElementReferenceService;
import org.eclipse.sapphire.services.ReferenceService;
import org.eclipse.sapphire.services.ServiceEvent;
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

	private List<DiagramConnectionPart> connections = new ArrayList<>();

	private SapphireDiagramEditorPagePart diagramPart;

	@Override
	protected void init() {
		super.init();

		diagramPart = context(SapphireDiagramEditorPagePart.class);
		initConnections();
	}

	private void initConnections() {
		Job job = (Job) (diagramPart.getLocalModelElement());

		for (FlowElement src : job.getFlowElements()) {
			Value<String> next = null;
			if (src instanceof Step) {
				next = ((Step) src).getNext();
			}
			if (src instanceof Flow) {
				next = ((Flow) src).getNext();
			}
			if (src instanceof Split) {
				next = ((Split) src).getNext();
			}
			if (next != null && next.content() != null && !next.content().trim().isEmpty()) {
				for (FlowElement target : job.getFlowElements()) {
					String targetId = target.getId().content();
					if (targetId != null && targetId.equals(next.content())) {
						connect(diagramPart.getDiagramNodePart(src), diagramPart.getDiagramNodePart(target),
								NEXT_ATTRIBUTE_CONNECTION_ID);
					}
				}
			}
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
			final BatchDiagramConnectionPart connectionPart = new BatchDiagramConnectionPart(node1, node2,
					connectionType);
			connections.add(connectionPart);

			final Element srcElement = node1.getLocalModelElement();
			connectionPart.init(diagramPart, srcElement,
					diagramPart.getDiagramConnectionDef(connectionType), Collections.<String, String> emptyMap());
			connectionPart.initialize();

			ReferenceValue<?, ?> referenceValue = (ReferenceValue<?, ?>) srcElement
					.property(Step.PROP_NEXT);
			ReferenceService<?> refService = referenceValue.service(ReferenceService.class);
			refService.attach(new Listener() {
				@Override
				public void handle(Event event) {
					FlowElement newTarget = ((Step)srcElement).getNext().target();
					
					if (newTarget == null) {
						BatchDiagramConnectionService.this.broadcast(new ConnectionDeleteEvent(connectionPart));
					} else {
						connectionPart.setNode2(diagramPart.getDiagramNodePart(newTarget));
						BatchDiagramConnectionService.this.broadcast(new ConnectionEndpointsEvent(connectionPart));
					}
				}
			});

			broadcast(new ConnectionAddEvent(connectionPart));

			return connectionPart;
		} else {
			return super.connect(node1, node2, connectionType);
		}
	}

	@Override
	public List<DiagramConnectionPart> list() {
		List<DiagramConnectionPart> allConnections = new ArrayList<>();

		allConnections.addAll(super.list());
		allConnections.addAll(connections);

		return allConnections;
	}

	// @Override
	// public List<IDiagramConnectionDef> possibleConnectionDefs(DiagramNodePart
	// srcNode) {
	// return
	// Collections.singletonList(diagramPart.getDiagramConnectionDef("NextConnection"));
	// // TODO
	// }
}
