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
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.koneki.ldt.metalua.MetaluaStateFactory;
import org.eclipse.koneki.ldt.module.AbstractLuaModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.naef.jnlua.LuaState;

public class LDTLuaTestCase extends TestCase {

	private LuaState luaState;
	final private String referenceFileAbsolutePath;
	final private String sourceFileAbsolutePath;
	final private List<File> luaPath;
	private static String LUA_TEST_FUNCTION = "test"; //$NON-NLS-1$
	private static String LUA_TEST_MODULE_NAME = "test"; //$NON-NLS-1$

	/**
	 * Just locate two files. File to test and a reference representing which AST tested file should produce.
	 * 
	 * @param modulePath
	 */
	public LDTLuaTestCase(final File sourceFilePath, final File referenceFilePath, final List<File> directoryListForLuaPath) {
		sourceFileAbsolutePath = sourceFilePath.getAbsolutePath();
		referenceFileAbsolutePath = referenceFilePath.getAbsolutePath();
		luaPath = directoryListForLuaPath;
		setName(sourceFilePath.getName());
	}

	@Before
	public void setUp() {

		// Create new Lua instance loaded with Metalua
		luaState = MetaluaStateFactory.newLuaState();

		// Yes, in all folder we will seek for .lua and .luac
		AbstractLuaModule.setLuaPath(luaState, luaPath, luaPath);

		// Load utility module
		luaState.getGlobal("require"); //$NON-NLS-1$
		luaState.pushString(LUA_TEST_MODULE_NAME);
		luaState.call(1, 1);
		luaState.setGlobal(LUA_TEST_MODULE_NAME);
	}

	@Test
	public void test() {
		/*
		 * Call Lua function which will generate api model form given source file and compare it to reference.
		 */
		luaState.getGlobal(LUA_TEST_MODULE_NAME);
		luaState.getField(-1, LUA_TEST_FUNCTION);
		luaState.pushString(sourceFileAbsolutePath);
		luaState.pushString(referenceFileAbsolutePath);
		luaState.call(2, 2);
		if (luaState.isBoolean(-2)) {
			assertTrue("Generated API model and reference one differ.", luaState.toBoolean(-2)); //$NON-NLS-1$
		} else {
			assertTrue(luaState.isNil(-2));
			fail(luaState.toString(-1));
		}
	}

	@After
	public void tearDown() {
		luaState.close();
	}

	@Override
	public void runTest() {
		test();
	}
}
