package org.jboss.tools.batch.ui.editor.internal.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.ReferenceValue;
import org.eclipse.sapphire.services.ReferenceService;
import org.eclipse.sapphire.ui.Point;
import org.eclipse.sapphire.ui.SapphireActionSystem;
import org.eclipse.sapphire.ui.diagram.ConnectionAddEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionBendpointsEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionDeleteEvent;
import org.eclipse.sapphire.ui.diagram.ConnectionEndpointsEvent;
import org.eclipse.sapphire.ui.diagram.DiagramConnectionPart;
import org.eclipse.sapphire.ui.diagram.def.IDiagramConnectionDef;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElement;
import org.jboss.tools.batch.ui.editor.internal.model.NextAttributeElement;

public class BatchDiagramConnectionPart extends DiagramConnectionPart {

	private NextAttributeElement srcElement;
	private FlowElement targetElement;

	private List<Point> bendpoints = new ArrayList<>();

	private BatchDiagramConnectionEventHandler eventHandler;
	private Listener listener;

	private ReferenceService<?> referenceService;
	private BatchDiagramConnectionService connectionService;

	public BatchDiagramConnectionPart(DiagramNodePart node1, DiagramNodePart node2,
			BatchDiagramConnectionService service, BatchDiagramConnectionEventHandler eventHandler) {
		this.srcElement = (NextAttributeElement) node1.getLocalModelElement();
		this.targetElement = (FlowElement) node2.getLocalModelElement();
		this.connectionService = service;
		this.eventHandler = eventHandler;
	}

	@Override
	protected void init() {
		initializeListeners();

		eventHandler.onConnectionAddEvent(new ConnectionAddEvent(this));
	}

	private void initializeListeners() {
		ReferenceValue<String, FlowElement> reference = srcElement.getNext();
		reference.target().refresh(); // must be refreshed, otherwise the
										// reference changed event not fired
		referenceService = reference.service(ReferenceService.class);
		listener = new Listener() {
			@Override
			public void handle(Event event) {
				FlowElement newTarget = srcElement.getNext().target();

				if (newTarget == null) {
					referenceService.detach(this);
					eventHandler.onConnectionDeleteEvent(new ConnectionDeleteEvent(BatchDiagramConnectionPart.this));
				} else if (newTarget != targetElement) {
					changeTargetElement(newTarget);
				}
			}
		};
		referenceService.attach(listener);
	}

	private void changeTargetElement(FlowElement newTarget) {
		targetElement = newTarget;
		srcElement.setNext(targetElement.getId().content());

		removeAllBendpoints();

		eventHandler.onConnectionEndpointsEvent(new ConnectionEndpointsEvent(this));
	}

	@Override
	public Set<String> getActionContexts() {
		Set<String> contextSet = new HashSet<String>();
		contextSet.add(SapphireActionSystem.CONTEXT_DIAGRAM_CONNECTION);
		contextSet.add(SapphireActionSystem.CONTEXT_DIAGRAM_CONNECTION_HIDDEN);
		return contextSet;
	}

	@Override
	public boolean removable() {
		return true;
	}

	@Override
	public void remove() {
		srcElement.setNext(null);
		eventHandler.onConnectionDeleteEvent(new ConnectionDeleteEvent(this));
	}

	@Override
	public String getId() {
		StringBuilder builder = new StringBuilder();
		builder.append(BachConnectionIdConst.NEXT_ATTRIBUTE_CONNECTION_ID);
		builder.append(connectionService.list().indexOf(this));
		return builder.toString();
	}

	@Override
	public String getConnectionTypeId() {
		return BachConnectionIdConst.NEXT_ATTRIBUTE_CONNECTION_ID;
	}

	@Override
	public IDiagramConnectionDef getConnectionDef() {
		return (IDiagramConnectionDef) definition;
	}

	@Override
	public DiagramConnectionPart reconnect(DiagramNodePart newSrc, DiagramNodePart newTargetNode) {
		changeTargetElement((FlowElement) newTargetNode.getLocalModelElement());
		return this;
	}

	@Override
	public boolean canEditLabel() {
		return false;
	}

	@Override
	public List<Point> getBendpoints() {
		return new ArrayList<>(bendpoints);
	}

	@Override
	public void removeAllBendpoints() {
		bendpoints.clear();
		broadcast(new ConnectionBendpointsEvent(this));
	}

	@Override
	public void resetBendpoints(List<Point> bendpoints) {
		this.bendpoints = bendpoints;
		broadcast(new ConnectionBendpointsEvent(this, true));
	}

	@Override
	public void addBendpoint(int index, int x, int y) {
		bendpoints.add(index, new Point(x, y));
		broadcast(new ConnectionBendpointsEvent(this));
	}

	@Override
	public void updateBendpoint(int index, int x, int y) {
		bendpoints.set(index, new Point(x, y));
		broadcast(new ConnectionBendpointsEvent(this));
	}

	@Override
	public void removeBendpoint(int index) {
		bendpoints.remove(index);
		broadcast(new ConnectionBendpointsEvent(this));
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public void setLabel(String newValue) {
	}

	@Override
	public Point getLabelPosition() {
		return null;
	}

	@Override
	public void setLabelPosition(Point newPos) {
	}

	@Override
	public Element getEndpoint1() {
		return srcElement;
	}

	@Override
	public Element getEndpoint2() {
		return targetElement;
	}

}
