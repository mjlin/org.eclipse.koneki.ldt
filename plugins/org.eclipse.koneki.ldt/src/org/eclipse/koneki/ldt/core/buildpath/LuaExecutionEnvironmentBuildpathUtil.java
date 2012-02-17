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
import org.eclipse.core.runtime.Path;

public final class LuaExecutionEnvironmentBuildpathUtil {

	private LuaExecutionEnvironmentBuildpathUtil() {
	}

	public static boolean isLuaExecutionEnvironmentContainer(IPath containerPath) {
		// TODO implement if

		// should start by and have 3 segment (type/EE ID/EE version)
		// DLTKCore.USER_LIBRARY_CONTAINER_ID

		return true;
	}

	public static IPath getLuaExecutionEnvironmentContainerPath(LuaExecutionEnvironment env) {
		return new Path("org.eclipse.koneki.ldt.ExecutionEnvironmentContainer/lua/5.1");
	}
}
