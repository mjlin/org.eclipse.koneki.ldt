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
package org.eclipse.koneki.ldt.parser.lua.tests;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.koneki.ldt.parser.lua.internal.tests.LuaTestModuleRunner;
import org.junit.Before;
import org.junit.Test;

/**
 * Runs lua test function on a lua file and its reference one.
 * 
 * The idea is to call a lua function with the paths of tested lua file and reference one.
 */
public class LDTLuaTestCase extends TestCase {

	final private String testModuleName;
	final private List<String> luaPath;
	final private String referenceFileAbsolutePath;
	final private String sourceFileAbsolutePath;

	/** Actual test is perfomed by this object */
	private LuaTestModuleRunner luaRunner;

	/**
	 * Just locate two files. File to test and a reference representing which AST tested file should produce.
	 * 
	 * @param testModuleName
	 * 
	 * @param modulePath
	 */
	public LDTLuaTestCase(final String testSuiteName, final String testModuleName, final IPath inputFilePath, final IPath referenceFilePath,
			final List<String> directoryListForLuaPath) {
		this.testModuleName = testModuleName;
		sourceFileAbsolutePath = inputFilePath.toOSString();
		referenceFileAbsolutePath = referenceFilePath.toOSString();
		luaPath = directoryListForLuaPath;

		// The Module name is
		String testName = MessageFormat.format("{0}.{1}", testSuiteName, inputFilePath.lastSegment()); //$NON-NLS-1$
		setName(testName);
	}

	@Before
	public void setUp() {
		luaRunner = new LuaTestModuleRunner(testModuleName, sourceFileAbsolutePath, referenceFileAbsolutePath, luaPath, filesToCompile());
	}

	@Test
	public void test() {
		// Run test on lua side
		try {
			luaRunner.run();
		} catch (final CoreException e) {
			fail(e.getMessage());
		}
	}

	@Override
	public void runTest() {
		test();
	}

	protected List<String> filesToCompile() {
		return Collections.emptyList();
	}
}
