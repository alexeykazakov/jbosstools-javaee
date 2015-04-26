package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Reader;

/**
 * Content proposal applicable to {@code <reader>}'s {@code ref} attribute.
 * 
 * @author Tomas Milata
 */
public class ItemReaderRefProposalService extends AbstractBatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { BatchArtifactType.ITEM_READER };
	
	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Reader.TYPE.image();
	}
}
