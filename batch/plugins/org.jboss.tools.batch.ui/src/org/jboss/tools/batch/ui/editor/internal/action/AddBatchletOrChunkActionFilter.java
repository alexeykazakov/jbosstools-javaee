package org.jboss.tools.batch.ui.editor.internal.action;

import org.eclipse.sapphire.ui.SapphireActionHandler;
import org.eclipse.sapphire.ui.SapphireActionHandlerFilter;
import org.jboss.tools.batch.ui.editor.internal.model.Batchlet;
import org.jboss.tools.batch.ui.editor.internal.model.Chunk;
import org.jboss.tools.batch.ui.editor.internal.model.Step;

public class AddBatchletOrChunkActionFilter extends SapphireActionHandlerFilter {

	private static final String SAPPHIRE_ADD = "Sapphire.Add.";
	private static final String SAPPHIRE_ADD_CHUNK = SAPPHIRE_ADD + Chunk.class.getSimpleName();
	private static final String SAPPHIRE_ADD_BATCHLET = SAPPHIRE_ADD + Batchlet.class.getSimpleName();

	@Override
	public boolean check(SapphireActionHandler handler) {
		String id = handler.getId();
		
		if (id.equals(SAPPHIRE_ADD_BATCHLET) || id.equals(SAPPHIRE_ADD_CHUNK)) {
			if (handler.getModelElement() instanceof Step) {
				Step step = (Step) handler.getModelElement();
				return step.getBatchletOrChunk().size() < 1;				
			}
		}
		return true;
	}

}
