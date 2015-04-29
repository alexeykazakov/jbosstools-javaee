package org.jboss.tools.batch.ui.itest.diagram;

import java.util.List;

import org.eclipse.sapphire.ui.SapphireEditorPagePart;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.Job;
import org.jboss.tools.batch.ui.editor.internal.model.Split;
import org.jboss.tools.batch.ui.itest.AbstractBatchSapphireEditorTest;

public class BatchDiagramNavigationTest extends AbstractBatchSapphireEditorTest {

	public void testTwoEditorTabs() {
		editor = openEditor("src/META-INF/batch-jobs/job-nested-flows.xml");
		List<SapphireEditorPagePart> editorParts = editor.getEditorPageParts();
		assertEquals("Unexpected number of editor tabs.", 2, editorParts.size());
	}

	public void testNestedFlowsTraversal() {
		editor = openEditor("src/META-INF/batch-jobs/job-nested-flows.xml");
		editor.setActivePage("Diagram");
	
		assertEquals("/myJob", getDiagramPage().getPageHeaderText());
		Job job = editor.getSchema();
		Flow flow1 = (Flow) job.getFlowElements().get(0);
	
		editor.changeDiagramContent(flow1);
		assertEquals("/myJob/flow1", getDiagramPage().getPageHeaderText());
		Split split = (Split) flow1.getFlowElements().get(0);
		Flow flowNested1 = split.getFlows().get(0);
	
		editor.changeDiagramContent(flowNested1);
		assertEquals("/myJob/flow1/split1/flowNested1", getDiagramPage().getPageHeaderText());
	
		editor.changeDiagramContent(flow1);
		assertEquals("/myJob/flow1", getDiagramPage().getPageHeaderText());
	
		editor.changeDiagramContent(job);
		assertEquals("/myJob", getDiagramPage().getPageHeaderText());
	}

}
