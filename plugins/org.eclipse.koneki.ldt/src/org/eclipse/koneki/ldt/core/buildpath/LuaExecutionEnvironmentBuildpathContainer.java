/*******************************************************************************
 * Copyright (c) 2011 Sierra Wireless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.koneki.ldt.core.buildpath;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IAccessRule;
import org.eclipse.dltk.core.IBuildpathAttribute;
import org.eclipse.dltk.core.IBuildpathContainer;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.internal.core.BuildpathEntry;
import org.eclipse.koneki.ldt.Activator;
import org.eclipse.koneki.ldt.core.buildpath.exceptions.LuaExecutionEnvironmentManifestException;

@SuppressWarnings("restriction")
public class LuaExecutionEnvironmentBuildpathContainer implements IBuildpathContainer {

	private final IPath path;

	public LuaExecutionEnvironmentBuildpathContainer(String eeID, String eeVersion, IPath path) {
		this.path = path;
	}

	@Override
	public IBuildpathEntry[] getBuildpathEntries() {
		try {
			final IPath[] eeBuildPathes = LuaExecutionEnvironmentBuildpathUtil.getExecutionEnvironmentBuildPath(path);
			final ArrayList<IBuildpathEntry> arrayList = new ArrayList<IBuildpathEntry>(eeBuildPathes.length);
			if (eeBuildPathes.length > 0) {
				for (final IPath buildPath : eeBuildPathes) {
					final IBuildpathEntry libEntry = DLTKCore.newLibraryEntry(buildPath, IAccessRule.EMPTY_RULES, new IBuildpathAttribute[0],
							BuildpathEntry.INCLUDE_ALL, BuildpathEntry.EXCLUDE_NONE, false, true);
					arrayList.add(libEntry);
				}
				return arrayList.toArray(new IBuildpathEntry[arrayList.size()]);
			}
		} catch (final LuaExecutionEnvironmentManifestException e) {
			final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.LuaExecutionEnvironmentBuildpathContainerInvalidEEManifest,
					e);
			Activator.log(status);
		} catch (final IOException e) {
			final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.LuaExecutionEnvironmentBuildpathContainerIOProblem, e);
			Activator.log(status);
		}
		return new IBuildpathEntry[0];
	}

	@Override
	public String getDescription() {
		try {
			final LuaExecutionEnvironment ee = LuaExecutionEnvironmentBuildpathUtil.getExecutionEnvironment(path);
			if (ee != null && (ee.getID() != null)) {
				final StringBuffer sb = new StringBuffer();
				final String id = ee.getID();
				// Appending ID with capital first letter
				if (id.length() > 0) {
					sb.append(id.substring(0, 1).toUpperCase());
					if (id.length() > 1) {
						sb.append(id.substring(1));
					}
					sb.append(' ');
				}
				sb.append(ee.getVersion());
				return sb.toString();
			}
		} catch (final LuaExecutionEnvironmentManifestException e) {
			final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.LuaExecutionEnvironmentBuildpathContainerInvalidEEManifest,
					e);
			Activator.log(status);
		} catch (final IOException e) {
			final Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.LuaExecutionEnvironmentBuildpathContainerIOProblem, e);
			Activator.log(status);
		}
		return Messages.LuaExecutionEnvironmentBuildpathContainerNoDescriptionAvailable;
	}

	@Override
	public int getKind() {
		// Not called at project creation nor project load.
		// Defined just in case ...
		return IBuildpathContainer.K_DEFAULT_SYSTEM;
	}

	@Override
	public IPath getPath() {
		return path;
	}

}
