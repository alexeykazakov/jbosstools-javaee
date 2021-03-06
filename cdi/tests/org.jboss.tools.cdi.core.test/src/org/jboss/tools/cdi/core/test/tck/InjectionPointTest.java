/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.core.test.tck;

import java.util.Collection;

import org.jboss.tools.cdi.core.CDIUtil;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IInjectionPointParameter;
import org.jboss.tools.common.java.IParametedType;

/**
 * @author Alexey Kazakov
 */
public class InjectionPointTest extends TCKTest {

	/**
	 * Section 3.7.1 - Declaring a bean constructorThe bean was not found.
	 *  - All parameters of a bean constructor are injection points.
	 */
	public void testQualifierTypeAnnotatedConstructor() {
		IClassBean bean = getClassBean("JavaSource/org/jboss/jsr299/tck/tests/implementation/simple/lifecycle/Duck.java");
		Collection<IInjectionPointParameter> points = CDIUtil.getInjectionPointParameters(bean);
		assertEquals("There should be two injection point parameters in the bean.", 2, points.size());
	}

	/**
	 * Section 3.9.1 - Declaring an initializer method
	 *  - All initializer method parameters are injection points.
	 */
	public void testBindingTypeOnInitializerParameter() {
		IClassBean bean = getClassBean("JavaSource/org/jboss/jsr299/tck/tests/implementation/enterprise/lifecycle/Mainz.java");
		Collection<IInjectionPointParameter> points = CDIUtil.getInjectionPointParameters(bean);
		assertEquals("There should be two injection point parameters in the bean.", 1, points.size());
	}

	// https://jira.jboss.org/browse/JBIDE-6387 Type of a method parameter is null in case of generic method.
	public void testMethodParameter() {
		IClassBean bean = getClassBean("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/inject/FarmBroken.java");
		assertNotNull("Can't find the bean.", bean);
		Collection<IInjectionPoint> injections = bean.getInjectionPoints();
		for (IInjectionPoint injectionPoint : injections) {
			if(injectionPoint instanceof IInjectionPointParameter) {
				IInjectionPointParameter param = (IInjectionPointParameter)injectionPoint;
				IParametedType type = param.getType();
				assertNotNull("Type of the parameter is null", type);
				assertNotNull("Signature of parameter type is null", type.getSignature());
			}
		}
	}

	public void testObserverMethodParameters() {
		IClassBean bean = getClassBean("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/inject/AnimalObserver.java");
		assertNotNull("Can't find the bean.", bean);
		Collection<IInjectionPoint> injections = bean.getInjectionPoints();
		assertEquals(1, injections.size());
	}

	public void testDisposerMethodParameters() {
		IClassBean bean = getClassBean("JavaSource/org/jboss/jsr299/tck/tests/jbt/validation/disposers/DisposerOk.java");
		assertNotNull("Can't find the bean.", bean);
		Collection<IInjectionPoint> injections = bean.getInjectionPoints();
		assertEquals(2, injections.size());
	}

	public void testGetInjections() {
		Collection<IInjectionPoint> ps = cdiProject.getInjections("org.jboss.jsr299.tck.tests.lookup.injection.Fox");
		assertTrue(!ps.isEmpty());

		ps = cdiProject.getInjections("org.jboss.jsr299.tck.tests.context.dependent.Tarantula");
		assertTrue(!ps.isEmpty());
	}

	public void testParameterInjectionsWithQualifierWithMemberValues() {
		IInjectionPointParameter injectionPoint = getInjectionPointParameter("JavaSource/org/jboss/jsr299/tck/tests/lookup/injection/NamedParameters.java", "setA");
		assertNotNull(injectionPoint);
		Collection<IBean> resolved = cdiProject.getBeans(false, injectionPoint);
		assertTrue(resolved.isEmpty());

		injectionPoint = getInjectionPointParameter("JavaSource/org/jboss/jsr299/tck/tests/lookup/injection/NamedParameters.java", "setABC");
		assertNotNull(injectionPoint);
		resolved = cdiProject.getBeans(false, injectionPoint);
		assertFalse(resolved.isEmpty());
	}

	public void testDefaultValuesInQualifierProperties() {
		IInjectionPoint injectionPoint = getInjectionPointField("JavaSource/org/jboss/jsr299/tck/tests/jbt/lookup/qualifier/MyBean.java", "i");
		assertNotNull(injectionPoint);
		Collection<IBean> resolved = cdiProject.getBeans(false, injectionPoint);
		assertFalse(resolved.isEmpty());
	}
}