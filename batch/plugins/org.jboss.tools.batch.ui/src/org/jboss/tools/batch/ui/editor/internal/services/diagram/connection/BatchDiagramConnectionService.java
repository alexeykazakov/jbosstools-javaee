package org.jboss.tools.batch.ui.editor.internal.services.diagram.connection;

import static org.jboss.tools.batch.ui.editor.internal.services.diagram.connection.BachtConnectionIdConst.NEXT_ATTRIBUTE_CONNECTION_ID;

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

/**
 * A custom implementation of Sapphire connection service. It provides adapted
 * methods for the connection using {@code next} attribute, i.e. @
 * {@link BachtConnectionIdConst#NEXT_ATTRIBUTE_CONNECTION_ID} and delegates call
 * to the standard service for other connection types.
 * 
 * @author Tomas Milata
 */
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

	/**
	 * Exposes the custom implementation when connectionType is
	 * {@link BachtConnectionIdConst#NEXT_ATTRIBUTE_CONNECTION_ID}, uses standard
	 * implementation otherwise.
	 * 
	 * @param connectionType
	 *            id as specified in the .sdef file
	 */
	@Override
	public boolean valid(DiagramNodePart node1, DiagramNodePart node2, String connectionType) {
		if (NEXT_ATTRIBUTE_CONNECTION_ID.equals(connectionType)) {
			return valid(node1, node2);
		} else {
			return super.valid(node1, node2, connectionType);
		}
	}

	/**
	 * Exposes the custom implementation when connectionType is
	 * {@link BachtConnectionIdConst#NEXT_ATTRIBUTE_CONNECTION_ID}, uses standard
	 * implementation otherwise.
	 * 
	 * @param connectionType
	 *            id as specified in the .sdef file
	 */
	@Override
	public DiagramConnectionPart connect(DiagramNodePart node1, DiagramNodePart node2, String connectionType) {

		if (NEXT_ATTRIBUTE_CONNECTION_ID.equals(connectionType)) {
			return connect(node1, node2);
		} else {
			return super.connect(node1, node2, connectionType);
		}
	}

	/**
	 * @return list of all connections of all types
	 */
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

	/**
	 * A connection from a {@code <step>}, {@code <split>} or {@code <flow>} to
	 * a {@code <step>}, {@code <split>}, {@code <flow>} or {@code <decision>}
	 * can be created iff target has an id, source is different than target and
	 * same connection does not exist yet.
	 */
	private boolean valid(DiagramNodePart node1, DiagramNodePart node2) {
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
	}

	/**
	 * Connects two nodes in the model and initializes the Sapphire part.
	 */
	private DiagramConnectionPart connect(DiagramNodePart node1, DiagramNodePart node2) {
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
	}

	/**
	 * Initializes diagram connections according to the model.
	 */
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

	/**
	 * Attaches a listener for changes in the value of the {@code next}
	 * attribute.
	 */
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

	/**
	 * Creates, initializes and returns a Sapphire connection part and adds it
	 * to the existing connections.
	 * 
	 * @param src
	 * @param target
	 * @return the new connection part
	 */
	private DiagramConnectionPart addConnectionPart(DiagramNodePart src, DiagramNodePart target) {

		NextAttributeConnectionPart connectionPart = new NextAttributeConnectionPart(src, target, this, eventHandler);
		connectionPart.init(diagramPart, src.getLocalModelElement(),
				diagramPart.getDiagramConnectionDef(NEXT_ATTRIBUTE_CONNECTION_ID),
				Collections.<String, String> emptyMap());
		connectionPart.initialize();

		nodesConnectionsMap.put((NextAttributeElement) src.getLocalModelElement(),
				(FlowElement) target.getLocalModelElement());
		connections.add(connectionPart);
		return connectionPart;
	}

	/**
	 * Manages the state of existing connections and notifies Sapphire framework
	 * about changes via broadcast events.
	 */
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
