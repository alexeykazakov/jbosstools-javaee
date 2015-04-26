package org.jboss.tools.batch.ui.editor.internal.action.diagram;

import org.eclipse.sapphire.ui.SapphireCondition;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;

/**
 * Ensures the "Go Up" action is shown only when current Batch diagram editor input is a nested flow.
 * 
 * @author Tomas Milata
 *
 */
public class OpenParentActionCondition extends SapphireCondition {

	@Override
	protected boolean evaluate() {
		return getPart().getLocalModelElement() instanceof Flow;
	}

}
