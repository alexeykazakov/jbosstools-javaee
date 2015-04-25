package org.jboss.tools.batch.ui.editor.internal.extensions;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.LoggingService;
import org.eclipse.sapphire.Sapphire;
import org.eclipse.sapphire.modeling.el.Function;
import org.eclipse.sapphire.modeling.el.FunctionContext;
import org.eclipse.sapphire.modeling.el.FunctionException;
import org.eclipse.sapphire.modeling.el.FunctionResult;
import org.jboss.tools.batch.ui.editor.internal.model.Flow;
import org.jboss.tools.batch.ui.editor.internal.model.Job;
import org.jboss.tools.batch.ui.editor.internal.model.Split;

public class BatchPathFunction extends Function {

	private static final char SEPARATOR = '/';

	@Override
	public String name() {
		return "BatchPath";
	}

	@Override
	public FunctionResult evaluate(FunctionContext context) {

		return new FunctionResult(this, context) {

			@Override
			protected Object evaluate() throws FunctionException {
				StringBuilder path = new StringBuilder();

				try {
					Element element = cast(operand(0), Element.class);
					do {
						if (element instanceof Job) {
							path.insert(0, ((Job) element).getId().content());
						} else if (element instanceof Flow) {
							path.insert(0, ((Flow) element).getId().content());
						} else if (element instanceof Split) {
							path.insert(0, ((Split) element).getId().content());
						}
						path.insert(0, SEPARATOR);

						if (element.parent() != null) {
							element = element.parent().element();
						} else {
							break;
						}

					} while (element != null);

				} catch (ClassCastException e) {
					Sapphire.service(LoggingService.class).log(e);
				}

				return path.toString();
			}
		};
	}

}
