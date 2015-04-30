package org.jboss.tools.batch.ui.editor.internal.services.diagram.connection;

import static org.jboss.tools.batch.ui.editor.internal.services.diagram.connection.BachtConnectionIdConst.NEXT_ATTRIBUTE_CONNECTION_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * {@link BachtConnectionIdConst#NEXT_ATTRIBUTE_CONNECTION_ID} and delegates
 * call to the standard service for other connection types.
 * 
 * @author Tomas Milata
 */
public class BatchDiagramConnectionService extends StandardConnectionService {

	private List<DiagramConnectionPart> connections;
	private Set<NextAttributeElement> nextAttributeElements = new HashSet<>();

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
	 * {@link BachtConnectionIdConst#NEXT_ATTRIBUTE_CONNECTION_ID}, uses
	 * standard implementation otherwise.
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
	 * {@link BachtConnectionIdConst#NEXT_ATTRIBUTE_CONNECTION_ID}, uses
	 * standard implementation otherwise.
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
		FlowElement target = (FlowElement) node2.getLocalModelElement();
		String nextId = target.getId().content();
		NextAttributeElement src = (NextAttributeElement) node1.getLocalModelElement();
		src.setNext(nextId);

		FlowElement existingEndpoint = nodesConnectionsMap.get(node1.getLocalModelElement());
		if (existingEndpoint == null) {
			return addConnectionPart(src, target);
		} else {
			return null;
		}
	}

	/**
	 * Initializes diagram connections according to the model.
	 */
	private void initConnections() {
		connections = new ArrayList<>();

		FlowElementsContainer currentModelRoot = (FlowElementsContainer) diagramPart.getLocalModelElement();
		attachListenerForNewNodes(currentModelRoot.getFlowElements());

		for (FlowElement src : currentModelRoot.getFlowElements()) {
			if (src instanceof NextAttributeElement) {
				initializeNextAttributeElement(src);
			}

		}
	}

	/**
	 * Creates a connection if src already has target, stores the node among
	 * current nodes and attaches listener for target changes.
	 * 
	 * @param src
	 *            the source node
	 */
	private void initializeNextAttributeElement(FlowElement src) {
		NextAttributeElement nextElem = (NextAttributeElement) src;
		ReferenceValue<String, FlowElement> next = nextElem.getNext();

		if (next.target() != null) {
			addConnectionPart(nextElem, next.target());
		}

		nextAttributeElements.add(nextElem);
		attachListenerForNewConnection(nextElem);
	}

	/**
	 * Watches the list of flow elements and when a new element is added, a
	 * listener for changes is added to it.
	 * 
	 * @param flowElements
	 *            the list of elements to watch
	 */
	private void attachListenerForNewNodes(final ElementList<FlowElement> flowElements) {
		flowElements.attach(new FilteredListener<PropertyContentEvent>() {
			@Override
			protected void handleTypedEvent(PropertyContentEvent event) {
				for (FlowElement element : flowElements) {
					if (element instanceof NextAttributeElement && !nextAttributeElements.contains(element)) {
						initializeNextAttributeElement(element);
					}
				}
			}
		});
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
					addConnectionPart(element, element.getNext().target());
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
	private DiagramConnectionPart addConnectionPart(NextAttributeElement src, FlowElement target) {

		NextAttributeConnectionPart connectionPart = new NextAttributeConnectionPart(src, target, this, eventHandler);
		connectionPart.init(diagramPart, src, diagramPart.getDiagramConnectionDef(NEXT_ATTRIBUTE_CONNECTION_ID),
				Collections.<String, String> emptyMap());
		connectionPart.initialize();

		nodesConnectionsMap.put(src, target);
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
