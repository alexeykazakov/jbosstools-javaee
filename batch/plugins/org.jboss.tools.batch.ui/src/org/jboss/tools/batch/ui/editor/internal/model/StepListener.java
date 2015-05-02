package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.sapphire.ElementType;

/**
 * Represents {@code <listener>} child tags of {@code <listeners>} the parent of
 * which is a {@code <step>}.
 * 
 * @author Tomas Milata
 *
 */
public interface StepListener extends Listener, RefAttributeElement {
	ElementType TYPE = new ElementType(StepListener.class);
}
