package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Decision;

/**
 * Content proposal applicable to {@code <decision>}'s {@code ref} attribute.
 * 
 * @author Tomas Milata
 */
public class DeciderRefProposalService extends AbstractBatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { BatchArtifactType.DECIDER };
	
	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Decision.TYPE.image();
	}

	
}
