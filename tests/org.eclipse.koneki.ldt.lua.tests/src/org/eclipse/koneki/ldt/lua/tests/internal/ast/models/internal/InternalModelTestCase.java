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
package org.eclipse.koneki.ldt.lua.tests.internal.ast.models.internal;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.koneki.ldt.lua.tests.internal.ast.models.api.APIModelTestCase;

public class InternalModelTestCase extends APIModelTestCase {
	public InternalModelTestCase(final String testSuiteName, final String testFileName, final IPath sourceFilePath, final IPath referenceFilePath,
			final List<String> directoryListForLuaPath) {
		super(testSuiteName, testFileName, sourceFilePath, referenceFilePath, directoryListForLuaPath);
	}

}
