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

	public LuaExecutionEnvironment(final String identifier, final String eeversion) {
		id = identifier;
		version = eeversion;
	}

	public String getID() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	/**
	 * Path is accessible form {@link LuaExecutionEnvironmentManager#getPath(LuaExecutionEnvironment)}
	 * 
	 * @see LuaExecutionEnvironmentManager#getPath(LuaExecutionEnvironment)
	 */
	@Deprecated
	public IPath[] getSourcepath() {
		// return absolute path
		// perhaps should return a String or an URI, the most useful.
		return null;
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
