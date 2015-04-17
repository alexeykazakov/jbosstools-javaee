package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Writer;

public class ItemWriterRefProposalService extends BatchContentProposalService {

	@Override
	protected BatchArtifactType batchArtifactType() {
		return BatchArtifactType.ITEM_WRITER;
	}

	@Override
	protected ImageData image() {
		return Writer.TYPE.image();
	}
}
