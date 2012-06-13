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
package org.eclipse.koneki.ldt.parser.format.internal.tests;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.koneki.ldt.parser.lua.tests.LDTLuaAbstractTestSuite;

public class FormatterTestSuite extends LDTLuaAbstractTestSuite {

	private static final String FORMATTER_TEST_FOLDER = "tests/formatter"; //$NON-NLS-1$

	public FormatterTestSuite() throws CoreException {
		super("Formatter", FORMATTER_TEST_FOLDER, "lua"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
