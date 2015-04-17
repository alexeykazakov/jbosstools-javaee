package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import static org.jboss.tools.batch.core.BatchArtifactType.*;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Listener;

public class StepListenerRefProposalService extends BatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { STEP_LISTENER, CHUNK_LISTENER, ITEM_READ_LISTENER,
			ITEM_PROCESS_LISTENER, ITEM_WRITE_LISTENER, SKIP_READ_LISTENER, SKIP_PROCESS_LISTENER, SKIP_WRITE_LISTENER,
			RETRY_READ_LISTENER, RETRY_PROCESS_LISTENER, RETRY_WRITE_LISTENER };

	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Listener.TYPE.image();
	}

}
