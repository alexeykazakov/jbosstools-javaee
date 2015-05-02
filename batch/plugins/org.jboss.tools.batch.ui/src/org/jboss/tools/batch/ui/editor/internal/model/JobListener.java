package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.sapphire.ElementType;

/**
 * Represents {@code <listener>} child tags of {@code <listeners>} the parent of
 * which is a {@code <job>}.
 * 
 * @author Tomas Milata
 *
 */
public interface JobListener extends Listener, RefAttributeElement {
	ElementType TYPE = new ElementType(JobListener.class);
}
