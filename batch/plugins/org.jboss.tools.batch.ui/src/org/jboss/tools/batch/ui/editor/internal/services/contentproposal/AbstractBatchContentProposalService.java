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

/**
 * Abstract parent for all Sapphire content proposal services for the Batch
 * editor. Handlers the technical stuff so that implementations can just declare
 * desired type of content and its presentation.
 * 
 * @author Tomas Milata
 */
public abstract class AbstractBatchContentProposalService extends ContentProposalService {

	private IBatchProject batchProject;

	/**
	 * Prepares the instance of the project with batch nature to be used for
	 * queries and calls parent.
	 */
	@Override
	protected void init() {
		super.init();

		IProject project = context(Job.class).adapt(IProject.class);
		batchProject = BatchCorePlugin.getBatchProject(project, true);
	}

	/**
	 * The list of content proposals returned by the session object is a list of
	 * artifacts specified by the
	 * {@link AbstractBatchContentProposalService#batchArtifactType()} method
	 * that contain a value of the current filter in their name as a substring.
	 * 
	 * @return a new session object
	 */
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

	/**
	 * Implementation should return an array of artifact types from which the
	 * content proposal should be selected. The returned value should not be
	 * {@code null}.
	 * 
	 * @return desired artifact types
	 */
	protected abstract BatchArtifactType[] batchArtifactType();

	/**
	 * Implementation should return an image to be used in the content proposal
	 * visual presentation alongside the label.
	 * 
	 * @return image to use in the content proposal
	 */
	protected abstract ImageData image();

}
