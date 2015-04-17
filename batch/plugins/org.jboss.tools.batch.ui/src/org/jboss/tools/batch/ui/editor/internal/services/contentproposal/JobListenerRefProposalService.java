package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Listener;

public class JobListenerRefProposalService extends BatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { BatchArtifactType.JOB_LISTENER };
	
	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Listener.TYPE.image();
	}
}
