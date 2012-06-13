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
package org.eclipse.koneki.ldt.parser.lua.internal.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.koneki.ldt.module.AbstractLuaModule;
import org.eclipse.koneki.ldt.parser.lua.tests.Activator;

import com.naef.jnlua.LuaState;

/**
 * Run lua test function on two files, a source and a reference one.
 */
public class LuaTestModuleRunner extends AbstractLuaModule {

	final private List<String> path;
	final private String sourceFilePath;
	final private String referenceFilePath;

	private static String LUA_TEST_FUNCTION = "test"; //$NON-NLS-1$
	private static String LUA_TEST_MODULE_NAME = "test"; //$NON-NLS-1$

	public LuaTestModuleRunner(final String sourcePath, final String refPath, final List<String> localPath) {
		sourceFilePath = sourcePath;
		referenceFilePath = refPath;
		path = new ArrayList<String>();
		path.addAll(localPath);
	}

	@Override
	protected List<String> getLuaSourcePaths() {
		return path;
	}

	@Override
	protected List<String> getLuacSourcePaths() {
		return getLuaSourcePaths();
	}

	/** Create a {@link LuaState} which has Metalua and AST generation capabilities */
	@Override
	protected LuaState createLuaState() {
		final MetaluaModuleLoader metaluaModuleLoader = new MetaluaModuleLoader();
		return metaluaModuleLoader.getLuaState();
	}

	@Override
	protected String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	@Override
	protected String getModuleName() {
		return LUA_TEST_MODULE_NAME;
	}

	/**
	 * Call test function named {@value #LUA_TEST_FUNCTION}.
	 * 
	 * @throws CoreException
	 *             with message error when failure occurs.
	 */
	public void run() throws CoreException {

		// Run lua test function
		final LuaState luaState = loadLuaModule();
		luaState.getGlobal(getModuleName());
		luaState.getField(-1, LUA_TEST_FUNCTION);
		luaState.pushString(sourceFilePath);
		luaState.pushString(referenceFilePath);
		luaState.call(2, 2);

		// Get error message if there is any
		String errorMessage = null;
		if ((luaState.isBoolean(-2) && !luaState.toBoolean(-2)) || luaState.isNil(-2)) {
			errorMessage = luaState.toString(-1);
		}

		// Close lua instance
		luaState.close();

		// Notify error when needed
		if (errorMessage != null) {
			final Status status = new Status(Status.ERROR, getPluginID(), errorMessage);
			throw new CoreException(status);
		}
	}

}
