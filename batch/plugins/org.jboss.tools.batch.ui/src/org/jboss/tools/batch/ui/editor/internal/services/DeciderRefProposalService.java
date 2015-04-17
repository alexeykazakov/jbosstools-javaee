package org.jboss.tools.batch.ui.editor.internal.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.sapphire.services.ContentProposal;
import org.eclipse.sapphire.services.ContentProposalService;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.core.BatchCorePlugin;
import org.jboss.tools.batch.core.IBatchArtifact;
import org.jboss.tools.batch.core.IBatchProject;
import org.jboss.tools.batch.ui.editor.internal.model.Decision;
import org.jboss.tools.batch.ui.editor.internal.model.Job;

public class DeciderRefProposalService extends ContentProposalService {

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
				Collection<IBatchArtifact> artifacts = batchProject.getArtifacts(BatchArtifactType.DECIDER);
				List<ContentProposal> proposals = new ArrayList<>(artifacts.size());
				for (IBatchArtifact artifact : artifacts) {
					proposals.add(new ContentProposal(artifact.getName(), null, null, Decision.TYPE.image()));
				}
				return proposals;
			}
		};
	}

}
