package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Decision;

public class DeciderRefProposalService extends BatchContentProposalService {

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
