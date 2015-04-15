package org.jboss.tools.batch.ui.editor.internal.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.sapphire.services.ContentProposal;
import org.jboss.tools.batch.core.BatchArtifactType;
import org.jboss.tools.batch.core.BatchCorePlugin;
import org.jboss.tools.batch.core.IBatchArtifact;
import org.jboss.tools.batch.core.IBatchProject;
import org.jboss.tools.batch.ui.editor.internal.model.Job;

public class BatchletRefProposalService extends org.eclipse.sapphire.services.ContentProposalService {

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
				Collection<IBatchArtifact> artifacts = batchProject.getArtifacts(BatchArtifactType.BATCHLET);
				return artifacts.stream().map((a) -> new ContentProposal(a.getName())).collect(Collectors.toList());
			}
		};
	}

}
