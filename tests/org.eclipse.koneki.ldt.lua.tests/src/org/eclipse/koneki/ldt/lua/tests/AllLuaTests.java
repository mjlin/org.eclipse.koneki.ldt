/*******************************************************************************
 * Copyright (c) 2012 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.lua.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.koneki.ldt.lua.tests.internal.ast.models.api.APIModelTestSuite;

public class AllLuaTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllLuaTests.class.getName());
		suite.addTest(new APIModelTestSuite());

		return suite;
	}
}
