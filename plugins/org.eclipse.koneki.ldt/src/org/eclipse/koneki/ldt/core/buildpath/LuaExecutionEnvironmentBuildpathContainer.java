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
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IAccessRule;
import org.eclipse.dltk.core.IBuildpathAttribute;
import org.eclipse.dltk.core.IBuildpathContainer;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.internal.core.BuildpathEntry;

public class LuaExecutionEnvironmentBuildpathContainer implements IBuildpathContainer {

	private IPath path;

	public LuaExecutionEnvironmentBuildpathContainer(String eeID, String eeVersion, IPath path) {
		this.path = path;
	}

	@Override
	public IBuildpathEntry[] getBuildpathEntries() {
		// get LuaExecutionEnvironment
		// get sourcepath
		// LuaExecutionEnvironmentBuildpathUtil to build corresponding IbuildpathEntry

		// TO ADD A ZIP
		// IBuildpathEntry newBuiltinEntry = DLTKCore.newLibraryEntry(new Path("org.eclipse.dltk.core.environment.localEnvironment/C:",
		// "Users/sbernard/Documents/tmp/konekiproduct/luadocumentator32/docs"), IAccessRule.EMPTY_RULES, new IBuildpathAttribute[0],
		// BuildpathEntry.INCLUDE_ALL, BuildpathEntry.EXCLUDE_NONE, false, true);

		// TO ADD A ZIP
		// IBuildpathEntry newBuiltinEntry = DLTKCore.newBuiltinEntry(
		// IBuildpathEntry.BUILTIN_EXTERNAL_ENTRY.append("c:/Users/sbernard/Documents/tmp/sdk.zip"), IAccessRule.EMPTY_RULES,
		// new IBuildpathAttribute[0], BuildpathEntry.INCLUDE_ALL, BuildpathEntry.EXCLUDE_NONE, false, true);
		//
		// IBuildpathEntry newBuiltinEntry3 = DLTKCore.newBuiltinEntry(new Path("org.eclipse.dltk.core.environment.localEnvironment/C:",
		// "/Users/sbernard/Documents/tmp/sdk.zip"), IAccessRule.EMPTY_RULES, new IBuildpathAttribute[0], BuildpathEntry.INCLUDE_ALL,
		// BuildpathEntry.EXCLUDE_NONE, false, true);
		//
		// // TO ADD A EXT LIB
		// IBuildpathEntry newBuiltinEntry1 = DLTKCore.newLibraryEntry(new Path("org.eclipse.dltk.core.environment.localEnvironment/C:",
		// "/Users/sbernard/Documents/tmp/konekiproduct/luadocumentator32/docs"), IAccessRule.EMPTY_RULES, new IBuildpathAttribute[0],
		// BuildpathEntry.INCLUDE_ALL, BuildpathEntry.EXCLUDE_NONE, false, true);

		IBuildpathEntry newBuiltinEntry2 = DLTKCore.newLibraryEntry(new Path("org.eclipse.dltk.core.environment.localEnvironment/C:",
				"/Users/sbernard/Documents/tmp/sdk.zip"), IAccessRule.EMPTY_RULES, new IBuildpathAttribute[0], BuildpathEntry.INCLUDE_ALL,
				BuildpathEntry.EXCLUDE_NONE, false, true);

		return new IBuildpathEntry[] { newBuiltinEntry2 };
	}

	@Override
	public String getDescription() {
		return "Lua 5.1 Execution Environment";
	}

	@Override
	public int getKind() {
		// not sure we must use this "kind"
		return IBuildpathContainer.K_DEFAULT_SYSTEM;
	}

	@Override
	public IPath getPath() {
		return path;
	}

}
