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
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * Launch a bench of TestCase for each file to test in the given folder.
 * 
 * @author Marc Aubry <maubry@sierrawireless.com>
 * @author Kevin KIN-FOO <kkinfoo@sierrawireless.com>
 */
public abstract class LDTLuaAbstractTestSuite extends TestSuite {

	private static final String COMMON_LIB_FOLDER = "/lib"; //$NON-NLS-1$

	/**
	 * 
	 * @param name
	 *            Name of the suite
	 * @param folderPath
	 *            The project relative path of the folder containing inputs, references and test files
	 * @param referenceFileExtension
	 *            Extension of the reference file (e.g. "lua" or "serialized")
	 * 
	 */
	public LDTLuaAbstractTestSuite(final String name, final String folderPath, final String referenceFileExtension) throws CoreException {
		super();
		setName(name);

		try {
			// Retrieve folder
			final Bundle bundle = Activator.getDefault().getBundle();
			final URL ressource = bundle.getResource(folderPath);
			final Path folderAbsolutePath = new Path(FileLocator.toFileURL(ressource).getPath());

			// check test suite folder
			checkFolder(folderAbsolutePath, "This is not a directory and cannot contain test suite files and folders"); //$NON-NLS-1$

			// check input folder
			final File inputFolder = checkFolder(folderAbsolutePath.append(getInputFolderPath()),
					"This is not a directory and cannot contain test lua input files"); //$NON-NLS-1$

			// check reference folder
			final File referenceFolder = checkFolder(folderAbsolutePath.append(getReferenceFolderPath()),
					"This is not a directory and cannot contain test reference files"); //$NON-NLS-1$

			// Retrieve files
			for (final File inputFile : getRecursiveFileList(inputFolder)) {

				// Compute reference file name (with the according extension)
				final IPath inputFilePath = new Path(inputFile.getAbsolutePath());
				final String fileName = inputFile.getName();
				final String fileNameWithoutExt = fileName.substring(0, fileName.length() - inputFilePath.getFileExtension().length() - 1);
				final String referenceFileNameWithExt = MessageFormat.format("{0}.{1}", fileNameWithoutExt, referenceFileExtension); //$NON-NLS-1$

				// Compute reference absolute path
				final String folderRelativeFilePath = computeFolderRelativeFilePath(inputFolder.getAbsolutePath(), inputFile.getAbsolutePath());
				IPath referenceFilePath = new Path(referenceFolder.getAbsolutePath()).append(folderRelativeFilePath);
				referenceFilePath = referenceFilePath.removeLastSegments(1).append(referenceFileNameWithExt);

				// Check reference file
				final String errorMessage = MessageFormat.format("No reference file found for {0}.", folderRelativeFilePath); //$NON-NLS-1$
				final File referenceFile = checkFile(referenceFilePath, errorMessage);

				// Compute path to provide to test case
				final ArrayList<String> path = new ArrayList<String>();
				path.add(COMMON_LIB_FOLDER);
				path.add(folderPath);

				// Append test case
				addTest(createTestCase(inputFile, referenceFile, path));
			}
		} catch (final IOException e) {
			final String message = MessageFormat.format("Unable to locate {0}.", folderPath); //$NON-NLS-1$
			raiseCoreException(message, e);
		}
	}

	private File checkFile(final IPath referenceFilePath, final String errorMessage) throws CoreException {
		final File referenceFile = new File(referenceFilePath.toOSString());
		if (!referenceFile.exists()) {
			raiseCoreException(errorMessage, null);
		}
		return referenceFile;
	}

	private File checkFolder(final IPath folderAbosultePath, final String errorMessage) throws CoreException {
		final File folder = new File(folderAbosultePath.toOSString());
		if (!folder.isDirectory()) {
			String message = MessageFormat.format("{0}: {1}", errorMessage, folderAbosultePath); //$NON-NLS-1$
			raiseCoreException(message, null);
		}
		return folder;
	}

	private List<File> getRecursiveFileList(final File file) {
		return getRecursiveFileList(file, new ArrayList<File>());
	}

	private List<File> getRecursiveFileList(final File file, List<File> list) {

		// Loop over directory
		if (file.isDirectory()) {
			for (final File subfile : file.listFiles()) {
				getRecursiveFileList(subfile, list);
			}
			return list;
		}

		// Regular file
		list.add(file);
		return list;
	}

	private String computeFolderRelativeFilePath(String folderPath, String filePath) {
		return URI.create(folderPath).relativize(URI.create(filePath)).toString();
	}

	/**
	 * @return Input file root folder
	 */
	protected String getInputFolderPath() {
		return "input"; //$NON-NLS-1$
	}

	/**
	 * @return References file root folder
	 */
	protected String getReferenceFolderPath() {
		return "reference"; //$NON-NLS-1$
	}

	/**
	 * @return lua file implementing the test
	 */
	protected String getTestLuaPath() {
		return "test.lua"; //$NON-NLS-1$
	}

	/**
	 * Raise a core exception for logging
	 * 
	 * @param message
	 *            Message to log
	 * @param t
	 *            Related exception
	 * @throws CoreException
	 * 
	 */
	protected final void raiseCoreException(final String message, final Throwable t) throws CoreException {
		final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, message, t);
		throw new CoreException(status);
	}

	protected TestCase createTestCase(final File source, final File reference, final List<String> path) {
		return new LDTLuaTestCase(source, reference, path);
	}
}
