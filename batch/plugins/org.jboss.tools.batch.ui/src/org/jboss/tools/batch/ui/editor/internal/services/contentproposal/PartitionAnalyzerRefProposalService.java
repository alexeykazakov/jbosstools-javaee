package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import org.eclipse.sapphire.ImageData;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.ui.editor.internal.model.Partition;

public class PartitionAnalyzerRefProposalService extends BatchContentProposalService {

	private static final BatchArtifactType[] TYPES = { BatchArtifactType.PARTITION_ANALYZER };
	
	@Override
	protected BatchArtifactType[] batchArtifactType() {
		return TYPES;
	}

	@Override
	protected ImageData image() {
		return Partition.TYPE.image();
	}
}