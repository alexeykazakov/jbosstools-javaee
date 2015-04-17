package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Decision;

public class DeciderRefProposalService extends BatchContentProposalService {

	@Override
	protected BatchArtifactType batchArtifactType() {
		return BatchArtifactType.DECIDER;
	}

	@Override
	protected ImageData image() {
		return Decision.TYPE.image();
	}

	
}
