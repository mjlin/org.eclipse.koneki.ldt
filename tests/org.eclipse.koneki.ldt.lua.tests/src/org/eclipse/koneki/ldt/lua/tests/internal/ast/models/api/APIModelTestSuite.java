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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.eclipse.koneki.ldt.lua.tests.internal.utils.AbstractLuaTestSuite;

public class APIModelTestSuite extends AbstractLuaTestSuite {

	public APIModelTestSuite() {
		super("apimodel", "tests/apimodel", "serialized.lua"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	protected TestCase createTestCase(final String testModuleName, final IPath source, final IPath ref, final List<String> path) {
		return new APIModelTestCase(getName(), testModuleName, source, ref, path);
	}
}
