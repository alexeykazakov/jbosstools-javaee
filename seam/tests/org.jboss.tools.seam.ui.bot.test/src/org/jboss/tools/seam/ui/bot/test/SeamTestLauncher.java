package org.jboss.tools.seam.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.seam.ui.bot.test.create.CreateSeamRuntimes;
import org.jboss.tools.seam.ui.bot.test.create.CreateSeamProjects;
import org.jboss.tools.seam.ui.bot.test.create.CreateForms;
import org.jboss.tools.seam.ui.bot.test.create.CreateActions;
import org.jboss.tools.seam.ui.bot.test.create.CreateConversations;
import org.jboss.tools.seam.ui.bot.test.create.CreateEntities;
import org.jboss.tools.seam.ui.bot.test.create.DeleteSeamProjects;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;



/**
 * 
 * This is a swtbot testcase for an eclipse application.
 * 
 */
public class SeamTestLauncher {
	public static Test suite(){
		TestSuite suite = new TestSuite("Seam tests");
		suite.addTestSuite(CreateSeamRuntimes.class);
		suite.addTestSuite(CreateSeamProjects.class);
		suite.addTestSuite(CreateForms.class);
		suite.addTestSuite(CreateActions.class);
		suite.addTestSuite(CreateConversations.class);
		suite.addTestSuite(CreateEntities.class);
		return suite;
	}
}