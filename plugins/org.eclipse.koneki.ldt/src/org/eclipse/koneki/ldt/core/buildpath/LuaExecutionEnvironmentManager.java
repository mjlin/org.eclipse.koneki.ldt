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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.koneki.ldt.Activator;

public final class LuaExecutionEnvironmentManager {

	private LuaExecutionEnvironmentManager() {
	}

	public static LuaExecutionEnvironment getExecutionEnvironment(String eeID, String eeVersion) {
		// search in the installed execution environment the good one.
		// return it or null if it doesn't exist (or perhaps should raise an exception)
		return null;
	}

	public static void installLuaExecutionEnvironment(String path) {
		// take a string or an Ipath or URI as input.
		// should surely throw Exception ?
	}

	private IPath getInstallDirectory() {
		return Activator.getDefault().getStateLocation().append(".EE");
	}

	public static List<LuaExecutionEnvironment> getInstalledExecutionEnvironments() {
		return new ArrayList<LuaExecutionEnvironment>();
	}
}
