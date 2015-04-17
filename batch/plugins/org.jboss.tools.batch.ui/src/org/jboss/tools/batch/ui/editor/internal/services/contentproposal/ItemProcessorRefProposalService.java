package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Processor;

public class ItemProcessorRefProposalService extends BatchContentProposalService {

	@Override
	protected BatchArtifactType batchArtifactType() {
		return BatchArtifactType.ITEM_PROCESSOR;
	}

	@Override
	protected ImageData image() {
		return Processor.TYPE.image();
	}
}
