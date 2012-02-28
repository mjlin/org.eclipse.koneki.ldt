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
package org.eclipse.koneki.ldt.core.buildpath;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.koneki.ldt.Activator;
import org.eclipse.koneki.ldt.core.buildpath.exceptions.LuaExecutionEnvironmentException;
import org.eclipse.koneki.ldt.core.buildpath.exceptions.LuaExecutionEnvironmentManifestException;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public final class LuaExecutionEnvironmentManager {
	private static final String INSTALLATION_FOLDER = "ee"; //$NON-NLS-1$

	private LuaExecutionEnvironmentManager() {
	}

	private static LuaExecutionEnvironment getExecutionEnvironmentFromCompressedFile(final String filePath)
			throws LuaExecutionEnvironmentManifestException, IOException {
		/*
		 * Search for manifest
		 */
		final ZipFile zipFile = new ZipFile(filePath);
		final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		String manifestString = null;
		while (zipEntries.hasMoreElements() && (manifestString == null)) {
			final ZipEntry zipEntry = zipEntries.nextElement();
			if (zipEntry.getName().endsWith(LuaExecutionEnvironmentConstants.MANIFEST_EXTENSION)) {
				final InputStream input = zipFile.getInputStream(zipEntry);
				manifestString = IOUtils.toString(input);
			}
		}
		if (manifestString == null) {
			final String message = Messages.bind(Messages.LuaExecutionEnvironmentManagerNoManifestProvided,
					LuaExecutionEnvironmentConstants.MANIFEST_EXTENSION);
			throw new LuaExecutionEnvironmentManifestException(message);
		}
		/*
		 * Match available package name
		 */
		Pattern namePattern = Pattern.compile("(?:^|\\s+)package\\s*=\\s*(?:[\"']|\\[*)([\\w.]+)"); //$NON-NLS-1$
		String name = null;
		Matcher matcher = namePattern.matcher(manifestString);
		if (matcher.find() && matcher.groupCount() > 0) {
			name = matcher.group(1);
		}

		/*
		 * Match available version
		 */
		String version = null;
		namePattern = Pattern.compile("(?:^|\\s+)version\\s*=\\s*(?:[\"']|\\[*)([\\w.]+)"); //$NON-NLS-1$
		matcher = namePattern.matcher(manifestString);
		if (matcher.find() && matcher.groupCount() > 0) {
			version = matcher.group(1);
		}

		// Create object representing a valid Execution Environment
		if (name == null || version == null) {
			throw new LuaExecutionEnvironmentManifestException(Messages.LuaExecutionEnvironmentManagerNoPackageNameOrVersion);
		}
		return getExecutionEnvironment(name, version);
	}

	/**
	 * Will deploy files from a valid Execution Environment file in installation directory. File will be considered as installed when its name will be
	 * appended in {@link LuaExecutionEnvironmentConstants#PREF_EXECUTION_ENVIRONMENTS_LIST}
	 * 
	 * @param path
	 *            Path to file to deploy
	 * @return {@link LuaExecutionEnvironmentException} when deployment succeeded.
	 * @throws IOException
	 * @throws LuaExecutionEnvironmentException
	 *             for problem due to deployment.
	 */
	public static LuaExecutionEnvironment installLuaExecutionEnvironment(final String path) throws IOException, LuaExecutionEnvironmentException {
		/*
		 * Ensure there are no folder named like the one we are about to create
		 */
		final LuaExecutionEnvironment ee = getExecutionEnvironmentFromCompressedFile(path);
		if (getInstalledExecutionEnvironments().contains(ee)) {
			throw new LuaExecutionEnvironmentException(Messages.LuaExecutionEnvironmentManagerAlreadyInstalled);
		}
		final IPath eePath = getPath(ee);
		if (eePath != null && eePath.toFile().exists()) {
			final File file = eePath.toFile();
			try {
				if (file.isFile()) {
					file.delete();
				} else {
					FileUtils.deleteDirectory(file);
				}
			} catch (IOException e) {
				throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToCreateInstallationDirectory, e);
			}
		}

		// Loop over content
		ZipInputStream zipStream = null;
		FileInputStream input = null;
		FileOutputStream fileOutputStream = null;
		try {
			// Open given file
			input = new FileInputStream(path);
			zipStream = new ZipInputStream(new BufferedInputStream(input));

			// Check if file is valid
			if (!isExecutionEnvironmentFile(path)) {
				final File file = new File(path);
				final String notValidMessage = Messages.bind(Messages.LuaExecutionEnvironmentManagerFileNotValid, file.getName());
				throw new FileNotFoundException(notValidMessage);
			}

			// Create temporary directory
			final File installationDirectory = getInstallDirectory().append(ee.getEEIdentifier()).toFile();
			if (!installationDirectory.mkdir()) {
				throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToCreateInstallationDirectory);
			}
			for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
				/*
				 * Flush current file on disk
				 */
				final File outputFile = new File(installationDirectory, entry.getName());

				// Define output file
				fileOutputStream = new FileOutputStream(outputFile);
				if (entry.isDirectory()) {
					// Create sub directory if needed
					if (!outputFile.delete() || !outputFile.mkdir()) {
						final String message = Messages.bind(Messages.LuaExecutionEnvironmentManagerUnableToExtract, entry.getName());
						throw new IOException(message);
					}
				} else {
					// Inflate file
					IOUtils.copy(zipStream, fileOutputStream);
				}
				// Flush on disk
				fileOutputStream.flush();
			}
		} catch (IOException e) {
			throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToReadInFile, e);
		} finally {
			/*
			 * Make sure all streams are closed
			 */
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			if (input != null) {
				input.close();
			}
			if (zipStream != null) {
				zipStream.close();
			}
		}
		return ee;
	}

	/**
	 * Ensure a given file is an archive containing the right files to be an Execution Environment file.
	 * 
	 * @param path
	 *            Path of file to analyze
	 * @return true if file contains:
	 *         <ul>
	 *         <li>{@value #EE_FILE_DOCS_FOLDER}</li>
	 *         <li>{@value #EE_FILE_API_ARCHIVE}</li>
	 *         <li>ending with {@value LuaExecutionEnvironmentConstants#MANIFEST_EXTENSION}
	 *         </ul>
	 *         false else way.
	 * @throws IOException
	 */
	public static boolean isExecutionEnvironmentFile(final String path) throws IOException {
		// Open given file
		final ZipFile zipFile = new ZipFile(path);
		boolean hasDocs = false;
		boolean hasApi = false;
		boolean hasRockspec = false;
		final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {
			final ZipEntry zipEntry = zipEntries.nextElement();
			final String name = zipEntry.getName();

			// Check for "docs" folder
			if (LuaExecutionEnvironmentConstants.EE_FILE_DOCS_FOLDER.equals(name)) {
				hasDocs = zipEntry.isDirectory();
			} else if (LuaExecutionEnvironmentConstants.EE_FILE_API_ARCHIVE.equals(name)) {
				// Check for "api.zip"
				hasApi = !zipEntry.isDirectory();
			} else if (name.endsWith(LuaExecutionEnvironmentConstants.MANIFEST_EXTENSION)) {
				// Finally check for "*.rockspec"
				if (hasRockspec) {
					// There was already a rockspec, consider this SDK invalid
					return false;
				} else {
					hasRockspec = true;
				}
			}
		}
		return hasApi && hasDocs && hasRockspec;
	}

	private static IPath getInstallDirectory() {
		// Create installation directory if needed
		final IPath path = Activator.getDefault().getStateLocation().append(INSTALLATION_FOLDER);
		if (!path.toFile().exists()) {
			path.toFile().mkdir();
		}
		return path;
	}

	public static IPath getPath(final LuaExecutionEnvironment ee) {
		final IPath path = getInstallDirectory().append(ee.getEEIdentifier());
		if (path.toFile().exists())
			return path;
		return null;
	}

	public static LuaExecutionEnvironment getExecutionEnvironment(final String eeid, final String version) {
		final IPath pathToEE = getInstallDirectory().append(eeid + '-' + version);
		if (!pathToEE.toFile().exists()) {
			return null;
		}
		return new LuaExecutionEnvironment(eeid, version, pathToEE);
	}

	public static LuaExecutionEnvironment getExecutionEnvironment(final String eeidAndVersion) {

		// Detect hyphen as version and name are around it
		final int hyphenPosition = eeidAndVersion.lastIndexOf('-');
		if ((hyphenPosition == -1) || (hyphenPosition >= eeidAndVersion.length()) || (hyphenPosition == 0)) {
			return null;
		}

		// Extract Execution environment name
		final String name = eeidAndVersion.substring(0, hyphenPosition);
		// Skip hyphen and extract version name
		final String version = eeidAndVersion.substring(hyphenPosition + 1);
		return getExecutionEnvironment(name, version);
	}

	public static List<LuaExecutionEnvironment> getInstalledExecutionEnvironments() {
		final ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(), org.eclipse.koneki.ldt.Activator.PLUGIN_ID);
		final String preferenceString = store.getString(LuaExecutionEnvironmentConstants.PREF_EXECUTION_ENVIRONMENTS_LIST);
		final ArrayList<LuaExecutionEnvironment> list = new ArrayList<LuaExecutionEnvironment>();
		if (preferenceString == null || preferenceString.isEmpty()) {
			return list;
		}

		for (final String id : preferenceString.split(LuaExecutionEnvironmentConstants.EXECUTION_ENVIRONMENTS_LIST_SEPARATOR)) {
			list.add(getExecutionEnvironment(id));
		}
		return list;
	}
}
