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

import junit.framework.TestSuite;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Status;
import org.eclipse.koneki.ldt.parser.lua.tests.Activator;
import org.osgi.framework.Bundle;

public class ASTGenerationTestSuite extends TestSuite {

	public ASTGenerationTestSuite() throws CoreException {
		super();
		setName("API Model"); //$NON-NLS-1$
		try {
			final Bundle bundle = Activator.getDefault().getBundle();
			final URL ressource = bundle.getResource(IASTGenerationConstants.LUA_FILES_FOLDER);
			final String sourcesPath = FileLocator.toFileURL(ressource).getFile();
			final File sourcesDirectory = new File(sourcesPath);
			if (!sourcesDirectory.isDirectory()) {
				final String message = MessageFormat.format(
						"{0} is not a directory and cannot contain lua sources.", IASTGenerationConstants.LUA_FILES_FOLDER); //$NON-NLS-1$
				raiseCoreException(message, null);
			}
			for (final String filePath : getRecursiveFileList(sourcesDirectory)) {
				addTest(new ASTGenerationTestCase(filePath));
			}
		} catch (final MalformedURLException e) {
			final String message = MessageFormat.format("Unable to generate URL corresponding to {0}.", IASTGenerationConstants.LUA_FILES_FOLDER); //$NON-NLS-1$
			raiseCoreException(message, e);
		} catch (final IOException e) {
			final String message = MessageFormat.format("Unable to locate {0}.", IASTGenerationConstants.LUA_FILES_FOLDER); //$NON-NLS-1$
			raiseCoreException(message, e);
		}
	}

	private void raiseCoreException(final String message, final Throwable t) throws CoreException {
		final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, message, t);
		throw new CoreException(status);
	}

	private List<String> getRecursiveFileList(final File file) {
		return getRecursiveFileList(file, new ArrayList<String>());
	}

	private List<String> getRecursiveFileList(final File file, List<String> list) {

		// Loop over directory
		if (file.isDirectory()) {
			for (final File subfile : file.listFiles()) {
				getRecursiveFileList(subfile, list);
			}
			return list;
		}

		// Regular file
		list.add(file.getAbsolutePath());
		return list;
	}
}
