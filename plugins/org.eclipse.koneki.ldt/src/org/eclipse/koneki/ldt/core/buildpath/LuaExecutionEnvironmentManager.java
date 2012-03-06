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
import java.io.FileOutputStream;
import java.io.FilenameFilter;
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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathContainer;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.koneki.ldt.Activator;
import org.eclipse.koneki.ldt.core.buildpath.exceptions.LuaExecutionEnvironmentException;
import org.eclipse.koneki.ldt.core.buildpath.exceptions.LuaExecutionEnvironmentManifestException;

public final class LuaExecutionEnvironmentManager {
	private static final String INSTALLATION_FOLDER = "ee"; //$NON-NLS-1$

	private LuaExecutionEnvironmentManager() {

	}

	private static LuaExecutionEnvironment getExecutionEnvironmentFromCompressedFile(final String filePath)
			throws LuaExecutionEnvironmentManifestException, IOException {
		/*
		 * Search for manifest
		 */
		ZipFile zipFile = null;
		String manifestString = null;
		try {
			zipFile = new ZipFile(filePath);
			final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			while (zipEntries.hasMoreElements() && (manifestString == null)) {
				final ZipEntry zipEntry = zipEntries.nextElement();
				if (zipEntry.getName().endsWith(LuaExecutionEnvironmentConstants.MANIFEST_EXTENSION)) {
					final InputStream input = zipFile.getInputStream(zipEntry);
					manifestString = IOUtils.toString(input);
				}
			}
		} finally {
			if (zipFile != null)
				zipFile.close();
		}
		if (manifestString == null) {
			final String message = Messages.bind(Messages.LuaExecutionEnvironmentManagerNoManifestProvided,
					LuaExecutionEnvironmentConstants.MANIFEST_EXTENSION);
			throw new LuaExecutionEnvironmentManifestException(message);
		}

		return getLuaExceptionEnvironmentFromManifest(manifestString);
	}

	private static LuaExecutionEnvironment getInstalledExecutionEnvironmentFromDir(final File executionEnvironmentDirectory)
			throws LuaExecutionEnvironmentManifestException, IOException {
		if (!executionEnvironmentDirectory.exists() || !executionEnvironmentDirectory.isDirectory())
			return null;

		String manifestString = null;
		File[] manifests = executionEnvironmentDirectory.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(LuaExecutionEnvironmentConstants.MANIFEST_EXTENSION);
			}
		});
		if (manifests != null && manifests.length == 1) {
			InputStream manifestInputStream = null;
			try {
				manifestInputStream = new FileInputStream(manifests[0]);
				manifestString = IOUtils.toString(manifestInputStream);
			} finally {
				if (manifestInputStream != null)
					manifestInputStream.close();
			}
		}

		if (manifestString == null) {
			final String message = Messages.bind(Messages.LuaExecutionEnvironmentManagerNoManifestProvided,
					LuaExecutionEnvironmentConstants.MANIFEST_EXTENSION);
			throw new LuaExecutionEnvironmentManifestException(message);
		}

		return getLuaExceptionEnvironmentFromManifest(manifestString);
	}

	private static LuaExecutionEnvironment getLuaExceptionEnvironmentFromManifest(String manifestString)
			throws LuaExecutionEnvironmentManifestException {
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

		// get install path
		final IPath pathToEE = getInstallDirectory().append(name + '-' + version);

		return new LuaExecutionEnvironment(name, version, pathToEE);
	}

	public static void removeLuaExecutionEnvironment(final LuaExecutionEnvironment ee) throws LuaExecutionEnvironmentException {
		if (ee == null)
			throw new LuaExecutionEnvironmentException(Messages.LuaExecutionEnvironmentManagerNoEEProvided);
		final IPath pathToEE = getLuaExecutionEnvironmentPath(ee.getID(), ee.getVersion());
		if (pathToEE == null)
			throw new LuaExecutionEnvironmentException(Messages.LuaExecutionEnvironmentBuildpathUtilCannotGetEE);
		final File eeInstallationDir = pathToEE.toFile();

		if (!eeInstallationDir.exists())
			throw new LuaExecutionEnvironmentException(Messages.LuaExecutionEnvironmentManagerUnableToLocateEE);
		try {
			FileUtils.deleteDirectory(eeInstallationDir);
			refreshDLTKModel(ee);
		} catch (final IOException e) {
			throw new LuaExecutionEnvironmentException(Messages.LuaExecutionEnvironmentManagerUnableToCleanInstallDirectory, e);

		}
	}

	/**
	 * Will deploy files from a valid Execution Environment file in installation directory. File will be considered as installed when its name will be
	 * appended in {@link LuaExecutionEnvironmentConstants#PREF_EXECUTION_ENVIRONMENTS_LIST}
	 * 
	 * @param zipPath
	 *            Path to file to deploy
	 * @return {@link LuaExecutionEnvironmentException} when deployment succeeded.
	 * @throws IOException
	 * @throws LuaExecutionEnvironmentException
	 *             for problem due to deployment.
	 */
	public static LuaExecutionEnvironment installLuaExecutionEnvironment(final String zipPath) throws IOException, LuaExecutionEnvironmentException {
		/*
		 * Ensure there are no folder named like the one we are about to create
		 */
		final LuaExecutionEnvironment ee = getExecutionEnvironmentFromCompressedFile(zipPath);
		if (getInstalledExecutionEnvironments().contains(ee)) {
			throw new LuaExecutionEnvironmentException(Messages.LuaExecutionEnvironmentManagerAlreadyInstalled);
		}

		// prepare the directory where the Execution environment will be installed
		final IPath eePath = ee.getPath();
		if (eePath == null)
			throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToCreateInstallationDirectory);

		final File installDirectory = eePath.toFile();
		if (!installDirectory.exists()) {
			if (!installDirectory.mkdirs())
				throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToCreateInstallationDirectory);
		} else {
			if (installDirectory.isFile()) {
				if (!installDirectory.delete())
					throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToCleanInstallDirectory);
			} else {
				try {

					FileUtils.deleteDirectory(installDirectory);
					if (!installDirectory.mkdir())
						throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToCreateInstallationDirectory);
				} catch (IOException e) {
					throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToCleanInstallDirectory, e);
				}
			}
		}

		// Loop over content
		ZipInputStream zipStream = null;
		FileInputStream input = null;
		try {
			// Open given file
			input = new FileInputStream(zipPath);
			zipStream = new ZipInputStream(new BufferedInputStream(input));

			// Check if file is valid
			if (!isExecutionEnvironmentFile(zipPath)) {
				final String notValidMessage = Messages.bind(Messages.LuaExecutionEnvironmentManagerFileNotValid, zipPath);
				throw new LuaExecutionEnvironmentException(notValidMessage);
			}

			for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
				/*
				 * Flush current file on disk
				 */
				final File outputFile = new File(installDirectory, entry.getName());
				// Define output file
				if (entry.isDirectory()) {
					// Create sub directory if needed
					if (!outputFile.mkdir()) {
						final String message = Messages.bind(Messages.LuaExecutionEnvironmentManagerUnableToExtract, entry.getName());
						throw new IOException(message);
					}
				} else {
					// copy file
					FileOutputStream fileOutputStream = null;
					try {
						fileOutputStream = new FileOutputStream(outputFile);
						// Inflate file
						IOUtils.copy(zipStream, fileOutputStream);

						// Flush on disk
						fileOutputStream.flush();
					} finally {
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
					}
				}
			}
		} catch (IOException e) {
			throw new IOException(Messages.LuaExecutionEnvironmentManagerUnableToReadInFile, e);
		} finally {
			/*
			 * Make sure all streams are closed
			 */
			if (input != null) {
				input.close();
			}
			if (zipStream != null) {
				zipStream.close();
			}
		}
		refreshDLTKModel(ee);
		return ee;
	}

	/**
	 * Ensure a given file is an archive containing the right files to be an Execution Environment file.
	 * 
	 * @param path
	 *            Path of file to analyze
	 * @return true if file contains:
	 *         <ul>
	 *         <li>{@value #EE_FILE_API_ARCHIVE}</li>
	 *         <li>ending with {@value LuaExecutionEnvironmentConstants#MANIFEST_EXTENSION}
	 *         </ul>
	 *         false else way.
	 * @throws IOException
	 */
	private static boolean isExecutionEnvironmentFile(final String path) throws IOException {

		ZipFile zipFile = null;
		boolean hasApi = false;
		boolean hasRockspec = false;

		try {
			// Open given file
			zipFile = new ZipFile(path);
			final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			while (zipEntries.hasMoreElements()) {
				final ZipEntry zipEntry = zipEntries.nextElement();
				final String name = zipEntry.getName();

				// Check for "docs" folder
				if (LuaExecutionEnvironmentConstants.EE_FILE_API_ARCHIVE.equals(name)) {
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
		} finally {
			if (zipFile != null)
				zipFile.close();
		}

		return hasApi && hasRockspec;
	}

	private static IPath getInstallDirectory() {
		return Activator.getDefault().getStateLocation().append(INSTALLATION_FOLDER);
	}

	public static List<LuaExecutionEnvironment> getInstalledExecutionEnvironments() {
		// list of execution environment installed
		final ArrayList<LuaExecutionEnvironment> result = new ArrayList<LuaExecutionEnvironment>();

		// search in the install directory

		IPath installDirectoryPath = getInstallDirectory();
		File installDirectory = installDirectoryPath.toFile();
		if (installDirectory.exists() && installDirectory.isDirectory()) {
			File[] content = installDirectory.listFiles();
			for (File executionEnvironmentDirectory : content) {
				if (executionEnvironmentDirectory.exists() && executionEnvironmentDirectory.isDirectory()) {
					LuaExecutionEnvironment executionEnvironment;
					try {
						executionEnvironment = getInstalledExecutionEnvironmentFromDir(executionEnvironmentDirectory);
						if (executionEnvironment != null)
							result.add(executionEnvironment);
					} catch (LuaExecutionEnvironmentManifestException e) {
						Activator.logWarning(Messages.LuaExecutionEnvironmentManagerInstallEEDirectoryNotClean, e);
					} catch (IOException e) {
						Activator.logWarning(Messages.LuaExecutionEnvironmentManagerInstallEEDirectoryNotClean, e);
					}
				}
			}
		}

		return result;
	}

	private static IPath getLuaExecutionEnvironmentPath(final String name, final String version) {
		return getInstallDirectory().append(name + '-' + version);
	}

	public static LuaExecutionEnvironment getInstalledExecutionEnvironment(String name, String version)
			throws LuaExecutionEnvironmentManifestException, IOException {
		IPath luaExecutionEnvironmentPath = getLuaExecutionEnvironmentPath(name, version);
		return getInstalledExecutionEnvironmentFromDir(luaExecutionEnvironmentPath.toFile());
	}

	private static void refreshDLTKModel(LuaExecutionEnvironment ee) {
		try {
			// get path for this execution environment
			IPath containerPath = LuaExecutionEnvironmentBuildpathUtil.getLuaExecutionEnvironmentContainerPath(ee);

			// find all project which references it
			IScriptProject[] scriptProjects = DLTKCore.create(ResourcesPlugin.getWorkspace().getRoot()).getScriptProjects();
			ArrayList<IScriptProject> affectedProjects = new ArrayList<IScriptProject>();
			for (int i = 0; i < scriptProjects.length; i++) {
				IScriptProject scriptProject = scriptProjects[i];
				IBuildpathEntry[] entries = scriptProject.getRawBuildpath();
				for (int j = 0; j < entries.length; j++) {
					IBuildpathEntry entry = entries[j];
					if (entry.getEntryKind() == IBuildpathEntry.BPE_CONTAINER) {
						if (containerPath.equals(entry.getPath())) {
							affectedProjects.add(scriptProject);
							break;
						}
					}
				}
			}

			// update affected projects
			int length = affectedProjects.size();
			if (length == 0)
				return;
			IScriptProject[] projects = new IScriptProject[length];
			affectedProjects.toArray(projects);
			IBuildpathContainer[] containers = new IBuildpathContainer[length];
			if (ee != null) {
				LuaExecutionEnvironmentBuildpathContainer container = new LuaExecutionEnvironmentBuildpathContainer(ee.getID(), ee.getVersion(),
						containerPath);
				for (int i = 0; i < length; i++) {
					containers[i] = container;
				}
			}
			DLTKCore.setBuildpathContainer(containerPath, projects, containers, null);
		} catch (ModelException e) {
			Activator.logError("Unable to refresh Model after execution environment change", e); //$NON-NLS-1$
		}
	}
}
