package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Batchlet;

public class BatchletRefProposalService extends BatchContentProposalService {

	@Override
	protected BatchArtifactType batchArtifactType() {
		return BatchArtifactType.BATCHLET;
	}

	@Override
	protected ImageData image() {
		return Batchlet.TYPE.image();
	}
}
