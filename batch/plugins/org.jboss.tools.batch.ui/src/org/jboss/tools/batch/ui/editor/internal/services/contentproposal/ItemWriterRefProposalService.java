package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Writer;

public class ItemWriterRefProposalService extends BatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { BatchArtifactType.ITEM_WRITER };
	
	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Writer.TYPE.image();
	}
}