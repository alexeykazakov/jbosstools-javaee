package org.jboss.tools.batch.ui.itest.ca.sapphire;

import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.services.ContentProposalService;
import org.jboss.tools.batch.ui.editor.internal.model.Batchlet;
import org.jboss.tools.batch.ui.editor.internal.model.Chunk;
import org.jboss.tools.batch.ui.editor.internal.model.FlowElement;
import org.jboss.tools.batch.ui.editor.internal.model.Step;
import org.jboss.tools.batch.ui.editor.internal.services.contentproposal.BatchletRefProposalService;
import org.jboss.tools.batch.ui.editor.internal.services.contentproposal.ItemProcessorRefProposalService;
import org.jboss.tools.batch.ui.editor.internal.services.contentproposal.ItemReaderRefProposalService;
import org.jboss.tools.batch.ui.editor.internal.services.contentproposal.ItemWriterRefProposalService;
import org.jboss.tools.batch.ui.itest.AbstractBatchSapphireEditorTest;

public class ContentProposalTest extends AbstractBatchSapphireEditorTest {

	public void test() {
		editor = openEditor("/src/META-INF/batch-jobs/job-ca-2.xml");

		ElementList<FlowElement> elements = editor.getSchema().getFlowElements();
		for (FlowElement flowElement : elements) {
			if (flowElement.getId().content().equals("step1")) {
				checkStep1((Step) flowElement);
			} else if (flowElement.getId().content().equals("step2")) {
				checkStep2((Step) flowElement);
			}
		}

	}

	private void checkStep1(Step step) {
		Batchlet batchlet = (Batchlet) step.getBatchletOrChunk().get(0);
		BatchletRefProposalService batchletRefService = batchlet.getRef().service(BatchletRefProposalService.class);
		checkProposals(batchletRefService, 4);
	}

	private void checkStep2(Step step) {
		Chunk chunk = (Chunk) step.getBatchletOrChunk().get(0);

		ItemReaderRefProposalService readerRefService = chunk.getReader().getRef()
				.service(ItemReaderRefProposalService.class);
		checkProposals(readerRefService, 2);

		ItemWriterRefProposalService writerRefService = chunk.getWriter().getRef()
				.service(ItemWriterRefProposalService.class);
		checkProposals(writerRefService, 2);

		ItemProcessorRefProposalService processorRefService = chunk.getProcessor().content().getRef()
				.service(ItemProcessorRefProposalService.class);
		checkProposals(processorRefService, 1);
	}

	private void checkProposals(ContentProposalService refService, int count) {
		assertNotNull(refService);
		assertEquals(count, refService.session().proposals().size());
	}
}
