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
package org.eclipse.koneki.ldt.lua.tests.internal.ast.models.api;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.koneki.ldt.lua.tests.internal.utils.AbstractLuaAbstractTestSuite;

public class APIModelTestSuite extends AbstractLuaAbstractTestSuite {

	public APIModelTestSuite() {
		super("API Model", "tests/apimodel", "serialized"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	protected TestCase createTestCase(final File source, final File ref, final List<String> path) {
		return new APIModelTestCase(source, ref, path);
	}
}
