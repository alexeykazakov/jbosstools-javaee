package org.jboss.tools.batch.ui.editor.internal.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ui.Point;
import org.eclipse.sapphire.ui.SapphireActionSystem;
import org.eclipse.sapphire.ui.diagram.DiagramConnectionPart;
import org.eclipse.sapphire.ui.diagram.def.IDiagramConnectionDef;
import org.eclipse.sapphire.ui.diagram.editor.DiagramNodePart;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElement;
import org.jboss.tools.batch.ui.editor.internal.model.Split;
import org.jboss.tools.batch.ui.editor.internal.model.Step;

public class BatchDiagramConnectionPart extends DiagramConnectionPart {

	private DiagramNodePart node1;
	private DiagramNodePart node2;
	private String connectionType;
	private List<Point> bendpoints = new ArrayList<>();
	private String label;
	private Point labelPosition;

	public BatchDiagramConnectionPart(DiagramNodePart node1, DiagramNodePart node2, String connectionType) {
		this.node1 = node1;
		this.node2 = node2;
		this.connectionType = connectionType;

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
	}

	@Override
	public boolean removable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void remove() {
		Element element = node1.getLocalModelElement();
		if (element instanceof Step) {
			((Step) element).setNext(null);
		} else if (element instanceof Flow) {
			((Flow) element).setNext(null);
		} else if (element instanceof Split) {
			((Split) element).setNext(null);
		} else {
			throw new IllegalArgumentException(); // TODO
		}
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}

	@Override
	public String getConnectionTypeId() {
		return connectionType;
	}

	@Override
	public IDiagramConnectionDef getConnectionDef() {
		return (IDiagramConnectionDef) definition;
	}

	@Override
	public DiagramConnectionPart reconnect(DiagramNodePart newSrc, DiagramNodePart newTarget) {
		remove();
		return new BatchDiagramConnectionPart(newSrc, newTarget, connectionType);
	}

	@Override
	public boolean canEditLabel() {
		return false;
	}

	@Override
	public List<Point> getBendpoints() {
		return bendpoints;
	}

	@Override
	public void removeAllBendpoints() {
		bendpoints.clear();
	}

	@Override
	public void resetBendpoints(List<Point> bendpoints) {
		this.bendpoints = bendpoints;
	}

	@Override
	public void addBendpoint(int index, int x, int y) {
		bendpoints.add(index, new Point(x, y));
	}

	@Override
	public void updateBendpoint(int index, int x, int y) {
		Point point = bendpoints.get(index);
		point.setX(x);
		point.setY(y);
	}

	@Override
	public void removeBendpoint(int index) {
		bendpoints.remove(index);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String newValue) {
		label = newValue;
	}

	@Override
	public Point getLabelPosition() {
		return labelPosition;
	}

	@Override
	public void setLabelPosition(Point newPos) {
		labelPosition = newPos;
	}

	@Override
	public Element getEndpoint1() {
		return node1.getLocalModelElement();
	}

	@Override
	public Element getEndpoint2() {
		return node2.getLocalModelElement();
	}
	
	@Override
    public Set<String> getActionContexts()
    {
        Set<String> contextSet = new HashSet<String>();
        contextSet.add(SapphireActionSystem.CONTEXT_DIAGRAM_CONNECTION);
        contextSet.add(SapphireActionSystem.CONTEXT_DIAGRAM_CONNECTION_HIDDEN);
        return contextSet;    	
    }

	public void setNode2(DiagramNodePart nodePart) {
		node2 = nodePart;		
	}

}
