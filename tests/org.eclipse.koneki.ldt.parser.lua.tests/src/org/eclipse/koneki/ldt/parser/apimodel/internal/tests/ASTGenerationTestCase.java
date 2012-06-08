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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.koneki.ldt.metalua.MetaluaStateFactory;
import org.eclipse.koneki.ldt.module.AbstractLuaModule;
import org.eclipse.koneki.ldt.parser.ModelsBuilderLuaModule;
import org.eclipse.koneki.ldt.parser.apimodel.tests.Activator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

import com.naef.jnlua.LuaState;

public class ASTGenerationTestCase extends TestCase {

	private String testFileFullPath;
	private String referenceFileFullPath;
	private LuaState luaState;
	private static String COMPARISON_FUNCTION = "recut"; //$NON-NLS-1$
	private static List<File> luaPath;
	private static String MODULE_NAME = "tableutils"; //$NON-NLS-1$
	private static String MODULES_FOLDER = "/lib/"; //$NON-NLS-1$

	/**
	 * Just locate two files. File to test and a reference representing which AST tested file should produce.
	 * 
	 * @param modulePath
	 */
	public ASTGenerationTestCase(final String testFile) throws CoreException {

		/*
		 * Separate file name from extension
		 */

		// Extract file name
		final Path testFilePath = new Path(testFile);
		String testFileExtension = testFilePath.getFileExtension();

		/*
		 * Remove trailing extension
		 */
		final String lastSegment = testFilePath.lastSegment();
		if (lastSegment == null) {
			final String message = MessageFormat.format("Unable to map {0} to testing material.", testFile); //$NON-NLS-1$
			final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, message);
			throw new CoreException(status);
		}
		final String testFileName;
		if (testFileExtension != null) {
			testFileName = lastSegment.substring(0, lastSegment.length() - testFileExtension.length() - 1);
		} else {
			// If no extension is provided carry on with default
			testFileExtension = IASTGenerationConstants.LUA_FILES_EXTENSION;
			testFileName = lastSegment;
		}

		// Name test case after file name
		setName(testFileName);

		// Find related serialized file
		final Path referenceFilePath = new Path(MessageFormat.format("{0}{1}", testFileName, IASTGenerationConstants.SERIALIZED_FILES_EXTENSION)); //$NON-NLS-1$
		final String referenceFileRelativeName = new Path(IASTGenerationConstants.SERIALIZED_FILES_FOLDER).append(referenceFilePath).toFile()
				.getAbsolutePath();
		final String testFileRelativeName = MessageFormat.format(
				"{0}{1}.{2}", IASTGenerationConstants.LUA_FILES_FOLDER, testFileName, testFileExtension); //$NON-NLS-1$
		final Bundle bundle = Activator.getDefault().getBundle();
		try {
			// Ensure file locations are valid and available on disk
			final URL testFileURL = bundle.getResource(testFileRelativeName);
			final URL referenceFileURL = bundle.getResource(referenceFileRelativeName);
			testFileFullPath = FileLocator.toFileURL(testFileURL).getFile();
			referenceFileFullPath = FileLocator.toFileURL(referenceFileURL).getFile();
		} catch (final MalformedURLException e) {
			final String message = MessageFormat.format("Unable to generate test files URLs for {0}.", testFile); //$NON-NLS-1$
			raiseCoreException(message, e);
		} catch (final IOException e) {
			final String message = MessageFormat.format("Unable to locate test files for {0}.", testFile); //$NON-NLS-1$
			raiseCoreException(message, e);
		}
		try {
			/*
			 * Load modules
			 */

			// Load module related to current module
			luaPath = new ArrayList<File>(2);
			final URL moduleURL = bundle.getResource(MODULES_FOLDER);
			final String modulePath = FileLocator.toFileURL(moduleURL).getPath();
			luaPath.add(new File(modulePath));

			// Load module related parser though parser module
			final Bundle parserBundle = org.eclipse.koneki.ldt.parser.Activator.getDefault().getBundle();
			final URL parserModuleURL = parserBundle.getResource(ModelsBuilderLuaModule.EXTERNAL_LIB_PATH);
			final String externalModulePath = FileLocator.toFileURL(parserModuleURL).getPath();
			luaPath.add(new File(externalModulePath));

		} catch (final IOException e) {
			final String message = MessageFormat.format("Unable to load module {0}.", MODULE_NAME); //$NON-NLS-1$
			raiseCoreException(message, e);
		}
	}

	private void raiseCoreException(final String message, final Throwable t) throws CoreException {
		final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, message, t);
		throw new CoreException(status);
	}

	@Before
	public void setUp() {

		// Create new Lua instance loaded with Metalua
		luaState = MetaluaStateFactory.newLuaState();

		// Yes, in all folder we will seek for .lua and .luac
		AbstractLuaModule.setLuaPath(luaState, luaPath, luaPath);

		// Load utility module
		luaState.getGlobal("require"); //$NON-NLS-1$
		luaState.pushString(MODULE_NAME);
		luaState.call(1, 1);
		luaState.setGlobal(MODULE_NAME);
	}

	@Test
	public void test() {
		/*
		 * Call Lua function which will generate api model form given source file and compare it to reference.
		 */
		luaState.getGlobal(MODULE_NAME);
		luaState.getField(-1, COMPARISON_FUNCTION);
		luaState.pushString(testFileFullPath);
		luaState.pushString(referenceFileFullPath);
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
