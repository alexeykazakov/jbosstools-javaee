package org.jboss.tools.batch.ui.editor.internal.services.contentproposal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.sapphire.ImageData;
import org.eclipse.sapphire.services.ContentProposal;
import org.eclipse.sapphire.services.ContentProposalService;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.core.BatchCorePlugin;
import org.jboss.tools.batch.core.IBatchArtifact;
import org.jboss.tools.batch.core.IBatchProject;
import org.jboss.tools.batch.ui.editor.internal.model.Job;

public abstract class BatchContentProposalService extends ContentProposalService {

	private IBatchProject batchProject;

	@Override
	protected void init() {
		super.init();

		IProject project = context(Job.class).adapt(IProject.class);
		batchProject = BatchCorePlugin.getBatchProject(project, true);
	}

	@Override
	public Session session() {
		return new Session() {

			@Override
			protected List<ContentProposal> compute() {
				List<ContentProposal> proposals = new ArrayList<>();
				for (BatchArtifactType type : batchArtifactType()) {
					Collection<IBatchArtifact> artifacts = batchProject.getArtifacts(type);
					for (IBatchArtifact artifact : artifacts) {
						if (artifact.getName().contains(filter())) {
							proposals.add(new ContentProposal(artifact.getName(), null, null, image()));
						}
					}
				}
				return proposals;
			}
		};
	}

	protected abstract BatchArtifactType[] batchArtifactType();

	protected abstract ImageData image();

}
