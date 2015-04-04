/*************************************************************************************
 * Copyright (c) 2014 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.batch.ui.editor.internal.model;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.modeling.xml.RootXmlResource;
import org.eclipse.sapphire.ui.SapphireEditor;
import org.eclipse.sapphire.ui.def.DefinitionLoader;
import org.eclipse.sapphire.ui.forms.swt.MasterDetailsEditorPage;
import org.eclipse.sapphire.ui.swt.gef.SapphireDiagramEditor;
import org.eclipse.sapphire.ui.swt.xml.editor.XmlEditorResourceStore;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.text.ext.IMultiPageEditor;

public class JobXMLEditor extends SapphireEditor implements IMultiPageEditor{
	
	public static final int DESIGN_PAGE_INDEX = 0;
	public static final int DIAGRAM_PAGE_INDEX = 1;
	
	private Job jobModel;
	private StructuredTextEditor schemaSourceEditor;
	private SapphireDiagramEditor schemaDiagram;
	private MasterDetailsEditorPage design;

		@Override
		protected void createSourcePages() throws PartInitException {
			this.schemaSourceEditor = new StructuredTextEditor();
			this.schemaSourceEditor.setEditorPart(this);
			int index = addPage( this.schemaSourceEditor, getEditorInput() );
			setPageText( index, "Source" );
		}

		@Override
		protected Element createModel() {
			this.jobModel = Job.TYPE.instantiate(new RootXmlResource(new XmlEditorResourceStore(this, this.schemaSourceEditor)));
			return this.jobModel;
		}

		@Override
		protected void createDiagramPages() throws PartInitException {	
			this.schemaDiagram = new SapphireDiagramEditor(
					this, this.jobModel,
					DefinitionLoader.sdef( getClass() ).page( "DiagramPage" )
			);
			addEditorPage( DIAGRAM_PAGE_INDEX, this.schemaDiagram );
			
		}

		@Override
		protected void createFormPages() throws PartInitException {
			this.design = new MasterDetailsEditorPage(
					this, this.jobModel,
					DefinitionLoader.sdef( getClass() ).page( "design" )
			);
			addPage( DESIGN_PAGE_INDEX, this.design );
		}

		public Job getSchema() {
			return this.jobModel;
		}

		@Override
		public void doSave( final IProgressMonitor monitor ) {
			this.schemaDiagram.doSave(monitor);
			super.doSave(monitor);
		}

	
		public StructuredTextEditor getSourceEditor() {
			return schemaSourceEditor;
		}

		public MasterDetailsEditorPage getFormEditor() {
			return design;
		}

		public void changeDiagramContent(Element element) {
			SapphireDiagramEditor diagram = new SapphireDiagramEditor(
					this, element,
					DefinitionLoader.sdef( getClass()).page( "DiagramPage" ) // TODO
			);

			try {
				addEditorPage(JobXMLEditor.DIAGRAM_PAGE_INDEX, diagram);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			removePage(JobXMLEditor.DIAGRAM_PAGE_INDEX + 1);		
			setActiveEditor(diagram);
		}
		
		public void goUp(Flow child) {
			changeDiagramContent(child.parent().element());
		}
}
