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
package org.eclipse.koneki.ldt.parser.lua.tests.apimodel;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.koneki.ldt.parser.lua.tests.LDTLuaAbstractTestSuite;

public class APIModelTestSuite extends LDTLuaAbstractTestSuite {

	private static final String APIMODEL_TEST_FOLDER = "tests/apimodel"; //$NON-NLS-1$

	public APIModelTestSuite() throws CoreException {
		super("API Model", APIMODEL_TEST_FOLDER, "serialized"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected TestCase createTestCase(final File source, final File ref, final List<String> path) {
		return new APIModelTestCase(source, ref, path);
	}
}
