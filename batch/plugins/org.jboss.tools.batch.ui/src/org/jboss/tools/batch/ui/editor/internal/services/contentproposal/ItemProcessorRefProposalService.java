package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Processor;

/**
 * Content proposal applicable to {@code <processor>}'s {@code ref} attribute.
 * 
 * @author Tomas Milata
 */
public class ItemProcessorRefProposalService extends AbstractBatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { BatchArtifactType.ITEM_PROCESSOR };
	
	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Processor.TYPE.image();
	}
}
