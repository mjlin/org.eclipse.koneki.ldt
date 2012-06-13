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

import java.io.File;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.koneki.ldt.parser.lua.internal.tests.LuaTestModuleRunner;
import org.junit.Before;
import org.junit.Test;

/**
 * Runs lua test function on a lua file and its reference one.
 * 
 * The idea is to call a lua function with the paths of tested lua file and reference one.
 */
public class LDTLuaTestCase extends TestCase {

	final private List<String> luaPath;
	final private String referenceFileAbsolutePath;
	final private String sourceFileAbsolutePath;

	/** Actual test is perfomed by this object */
	private LuaTestModuleRunner luaRunner;

	/**
	 * Just locate two files. File to test and a reference representing which AST tested file should produce.
	 * 
	 * @param modulePath
	 */
	public LDTLuaTestCase(final File sourceFilePath, final File referenceFilePath, final List<String> directoryListForLuaPath) {
		sourceFileAbsolutePath = sourceFilePath.getAbsolutePath();
		referenceFileAbsolutePath = referenceFilePath.getAbsolutePath();
		luaPath = directoryListForLuaPath;
		setName(sourceFilePath.getName());
	}

	@Before
	public void setUp() {
		luaRunner = new LuaTestModuleRunner(sourceFileAbsolutePath, referenceFileAbsolutePath, luaPath, filesToCompile());
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
