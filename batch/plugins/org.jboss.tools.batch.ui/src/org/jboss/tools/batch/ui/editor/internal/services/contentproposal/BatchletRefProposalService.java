package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Batchlet;

/**
 * Content proposal applicable to {@code <batchlet>}'s {@code ref} attribute.
 * 
 * @author Tomas Milata
 */
public class BatchletRefProposalService extends AbstractBatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { BatchArtifactType.BATCHLET };

	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Batchlet.TYPE.image();
	}
}
