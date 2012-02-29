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
import org.eclipse.core.runtime.Path;
import org.eclipse.koneki.ldt.core.buildpath.exceptions.LuaExecutionEnvironmentManifestException;

public final class LuaExecutionEnvironmentBuildpathUtil {

	private LuaExecutionEnvironmentBuildpathUtil() {
	}

	public static boolean isLuaExecutionEnvironmentContainer(final IPath containerPath) {
		if (isValidEEPath(containerPath)) {
			final String eeid = getEEID(containerPath);
			final String eeVersion = getEEVersion(containerPath);
			try {
				return LuaExecutionEnvironmentManager.getInstalledExecutionEnvironment(eeid, eeVersion) != null;
			} catch (LuaExecutionEnvironmentManifestException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	public static IPath getLuaExecutionEnvironmentContainerPath(final LuaExecutionEnvironment env) {
		return new Path(LuaExecutionEnvironmentConstants.CONTAINER_PATH_START).append(env.getID()).append(env.getVersion());
	}

	public static String getEEID(final IPath eePath) {
		if (isValidEEPath(eePath)) {
			return eePath.segment(1);
		}
		return null;
	}

	public static String getEEVersion(final IPath eePath) {
		if (isValidEEPath(eePath)) {
			return eePath.segment(2);
		}
		return null;
	}

	public static LuaExecutionEnvironment getExecutionEnvironment(final IPath path) throws LuaExecutionEnvironmentManifestException, IOException {
		if (isValidEEPath(path)) {
			final String id = getEEID(path);
			final String version = getEEVersion(path);
			return LuaExecutionEnvironmentManager.getInstalledExecutionEnvironment(id, version);
		}
		return null;
	}

	public static IPath[] getExecutionEnvironmentBuildPath(final IPath path) throws LuaExecutionEnvironmentManifestException, IOException {
		if (isValidEEPath(path)) {
			final LuaExecutionEnvironment ee = getExecutionEnvironment(path);
			if (ee != null) {
				final IPath eepath = ee.getPath();
				if (eepath != null) {
					// Determine windows device
					String root = eepath.getDevice();
					if (root == null) {
						// If not available, switch to Unix device
						root = "/"; //$NON-NLS-1$
					}
					final IPath[] pathes = ee.getSourcepath();
					final ArrayList<IPath> arrayList = new ArrayList<IPath>(pathes.length);
					for (final IPath sourcePath : pathes) {
						final Path buildPath = new Path(LuaExecutionEnvironmentConstants.BUILD_PATH_DEVICE_PREFIX + root, sourcePath.toOSString());
						arrayList.add(buildPath);
					}
					return arrayList.toArray(new IPath[pathes.length]);
				}
			}
		}
		return new IPath[0];
	}

	private static boolean isValidEEPath(final IPath eePath) {
		final String[] segments = eePath.segments();
		return (segments.length == 3) && LuaExecutionEnvironmentConstants.CONTAINER_PATH_START.equals(segments[0]);
	}
}
