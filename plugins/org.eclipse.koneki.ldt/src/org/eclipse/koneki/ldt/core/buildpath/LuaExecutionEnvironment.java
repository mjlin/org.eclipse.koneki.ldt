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

import org.eclipse.core.runtime.IPath;

public class LuaExecutionEnvironment {

	private final String id;
	private final String version;
	private final IPath path;

	public LuaExecutionEnvironment(final String identifier, final String eeversion, final IPath pathToEE) {
		id = identifier;
		version = eeversion;
		path = pathToEE;
	}

	public String getID() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	public IPath[] getSourcepath() {
		if (path != null && path.toFile().exists()) {
			final IPath sourcePath = path.append(LuaExecutionEnvironmentConstants.EE_FILE_API_ARCHIVE);
			if (sourcePath.toFile().exists()) {
				return new IPath[] { path };
			}
		}
		return new IPath[0];
	}

	// TODO: Try implementation
	public IPath[] getDocumentatioPpath() {
		if (path != null && path.toFile().exists()) {
			final IPath sourcePath = path.append(LuaExecutionEnvironmentConstants.EE_FILE_DOCS_FOLDER);
			if (sourcePath.toFile().exists()) {
				return new IPath[] { path };
			}
		}
		return new IPath[0];
	}

	public String getEEIdentifier() {
		return getID() + "-" + getVersion(); //$NON-NLS-1$
	}

	@Override
	public String toString() {
		return getEEIdentifier();
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof LuaExecutionEnvironment))
			return false;
		final LuaExecutionEnvironment ee = (LuaExecutionEnvironment) o;
		return getEEIdentifier().equals(ee.getEEIdentifier()) && getVersion().equals(ee.getVersion());
	}

	@Override
	public int hashCode() {
		return getID().hashCode() + getVersion().hashCode();
	}
}
