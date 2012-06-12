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
package org.eclipse.koneki.ldt.parser.apimodel.internal.tests;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.koneki.ldt.parser.lua.tests.LDTLuaAbstractTestSuite;

public class ASTGenerationTestSuite extends LDTLuaAbstractTestSuite {

	private static final String APIMODEL_TEST_FOLDER = "tests/apimodel"; //$NON-NLS-1$

	public ASTGenerationTestSuite() throws CoreException {
		super("API Model", APIMODEL_TEST_FOLDER, "serialized"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
