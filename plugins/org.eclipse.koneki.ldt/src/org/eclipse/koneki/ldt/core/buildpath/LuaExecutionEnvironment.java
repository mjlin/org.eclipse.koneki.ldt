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

import org.eclipse.core.runtime.IPath;

public class LuaExecutionEnvironment {

	public String getID() {
		return "";
	}

	public String getVersion() {
		return "";
	}

	public IPath[] getSourcepath() {
		// return absolute path
		// perhaps should return a String or an URI, the most usefull.
		return null;
	}
}
